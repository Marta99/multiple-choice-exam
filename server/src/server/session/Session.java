package server.session;

import common.MultipleChoiceClient;
import common.MultipleChoiceServer;
import server.Exam;
import server.Professor;
import server.QuestionAdapter;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Session extends UnicastRemoteObject implements MultipleChoiceServer, ExamController {

    private final HashMap<String, MultipleChoiceClient> clients = new HashMap<>();
    private final String sessionID;
    private final List<QuestionAdapter> questions;
    private final Professor professor;
    private SessionState state;
    private final HashMap<String, Exam> exams;


    public Session(Professor professor, String sessionID, List<QuestionAdapter> questions) throws RemoteException {
        this.sessionID = sessionID;
        this.professor = professor;
        this.questions = questions;
        this.exams = new HashMap<>();
        this.professor.setSession(this);
        this.state = SessionState.OPENED;
    }

    @Override
    public String joinSession(MultipleChoiceClient client) throws RemoteException {
        try {
            String studentID = client.getUniversityID();
            if (this.state == SessionState.STARTED) {
                //TODO: Do it better from client part
                Professor.logger.warning("User " + studentID + " attempt to joinSession but exam has already started.");
                return "The Exam has already started.";
            } else if (this.state == SessionState.FINISHED) {
                Professor.logger.warning("User " + studentID + " attempt to joinSession but exam has finished.");
                return "The Exam has already finished.";
            }
            //TODO: Que passa quan hi ha 2 persones amb el mateix ID.
            this.clients.put(studentID, client);
            Professor.logger.info("User " + studentID + " is joining the session");
            professor.receiveMSG("Student " + studentID + " has joined the session " + sessionID + ".");
            professor.receiveMSG("Now there are " + this.clients.size() + " students in the session.");
        } catch (RemoteException e) {
            Professor.logger.warning("Lost connection of user ");
        }
        return "You have joined the session";

    }

    @Override
    public void receiveAnswer(MultipleChoiceClient c, int i) throws Exception {
        String studentID = c.getUniversityID();
        Exam exam = this.exams.get(studentID);
        if (exam.hasFinished()) {
            Professor.logger.warning("Student " + studentID + " has sent an answer.");
            c.receiveMSG("Your exam has finished.");
            return;
        }
        if (!(1 <= i && i <= exam.getLastQuestion().numAnswers())) {
            Professor.logger.warning("Student " + studentID + " has answered with an incorrect value.");
            c.receiveMSG("Your answer is not properly suitable for that question. The question is:");
            c.receiveQuestion(exam.getLastQuestion().getQuestion());
            return;
        }
        Professor.logger.info("Checking answer of student " + studentID);
        exam.evaluateLastQuestion(i);
        if (exam.hasNext())
            c.receiveQuestion(exam.next().getQuestion());
        else
            finishExamStudent(studentID, exam);
    }

    private void finishExamStudent(String ID, Exam exam) throws IOException {
        try {
            MultipleChoiceClient client = exam.getStudent();
            Professor.logger.info(ID + " is finishing the exam.");
            client.receiveGrade(exam.finish());
        } catch (RemoteException ex) {
            Professor.logger.warning("User " + ID + " has disconnected.");
        }
        professor.receiveMSG(ID + " has finished the exam with grade: " + exam.getGrade());
        clients.remove(ID);
        professor.receiveMSG("Now there are " + clients.size() + " students taking the exam.");
        if (clients.size() == 0) {
            Professor.logger.info("All the students have succesfully finished the exam");
            this.state = SessionState.FINISHED;
            professor.receiveMSG("The exam has finished.");
            savingGrades();
            synchronized (this) {
                this.notify();
            }
        }
    }

    private void savingGrades() throws IOException {
        Professor.logger.info("Saving the grades");
        professor.receiveGrades(exams);
    }

    @Override
    synchronized public void startExam() throws SessionException, IOException {
        if (this.state != SessionState.OPENED)
            throw new UnsupportedSessionStateException("Session is not opened");
        if (clients.size() == 0)
            throw new SessionException("There are no students in the session to start the session");
        this.state = SessionState.STARTED;
        Professor.logger.info("Exam has started");
        clients.forEach((s, c) -> exams.put(s, new Exam(c, questions)));
        for (Map.Entry<String, Exam> entry : exams.entrySet()) {
            var studentID = entry.getKey();
            var e = entry.getValue();
            QuestionAdapter q = e.next();
            var t = new Thread(() -> {
                try {
                    MultipleChoiceClient c = e.getStudent();
                    Professor.logger.info("Starting exam for user " + studentID);
                    c.receiveQuestion(q.getQuestion());
                } catch (IOException ioException) {
                    Professor.logger.warning("Could not connect with Student " + studentID);
                }
            });
            t.start();
        }
        Professor.logger.info("The exam has started for all users");
    }

    @Override
    public void finishExam() throws IOException {
        if (this.state == SessionState.OPENED) {
            professor.receiveMSG("It can not be possible to finish the exam");
            return;
        }
        this.state = SessionState.FINISHED;
        professor.receiveMSG("The exam has finished.");
        for (Map.Entry<String, Exam> entry : exams.entrySet().stream().filter(x -> !x.getValue().hasFinished()).collect(Collectors.toList()))
            finishExamStudent(entry.getKey(), entry.getValue());
        savingGrades();
    }

    @Override
    public boolean hasFinished() {
        return this.state == SessionState.FINISHED;
    }
}
