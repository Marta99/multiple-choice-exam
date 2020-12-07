package server.session;

import common.MultipleChoiceClient;
import common.MultipleChoiceServer;
import server.Exam;
import server.Professor;
import server.QuestionAdapter;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
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
        if (this.state == SessionState.STARTED) {
            //TODO: Do it better from client part and here
            System.out.println("STUDENT REJECTED");
            return "The Exam has already started.";
        } else if(this.state == SessionState.FINISHED) {
            System.out.println("STUDENT REJECTED");
            return "The Exam has already finished.";
        }
        this.clients.put(client.getUniversityID(), client);
        professor.receiveMSG("A student has joined the session " + sessionID + ".");
        professor.receiveMSG("Now there are " + this.clients.size() + " students in the session.");
        return "You have joined the session";
    }

    @Override
    public void receiveAnswer(MultipleChoiceClient c, int i) throws Exception {
        Exam exam = this.exams.get(c.getUniversityID());
        String studentID = c.getUniversityID();
        if (exam.hasFinished()){
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
        exam.evaluateLastQuestion(i);
        if (exam.hasNext())
            c.receiveQuestion(exam.next().getQuestion());
        else
            finishExamStudent(exam);
    }

    private void finishExamStudent(Exam exam) throws IOException {
        var c = exam.getStudent();
        String ID = c.getUniversityID();
        c.receiveGrade(exam.finish());
        c.finishSessionStudent();
        professor.receiveMSG(ID + " has finished the exam with grade: " + exam.getGrade());
        clients.remove(ID);
        professor.receiveMSG("Now there are " + clients.size() + " students taking the exam.");
        if(clients.size()==0) {
            savingGrades();
        }
    }

    private void savingGrades() throws IOException {
        professor.receiveMSG("The exam has finished.");
        professor.receiveMSG("Saving the grades...");
        professor.receiveGrades(exams);
        System.exit(0);
    }

    @Override
    public void startExam() throws SessionException, IOException {
        if (this.state != SessionState.OPENED)
            throw new UnsupportedSessionStateException("Session is not opened");
        if (clients.size() == 0)
            throw new SessionException("There are no students in the session to start the session");
        this.state = SessionState.STARTED;
        clients.forEach((s, c) -> exams.put(s, new Exam(c, questions)));
        for (var e : exams.values()) {
            QuestionAdapter q = e.next();
            MultipleChoiceClient c = e.getStudent();
            c.receiveQuestion(q.getQuestion());
        }
    }

    @Override
    public void finishExam() throws IOException {
        if (this.state == SessionState.OPENED) {
            professor.receiveMSG("It can not be possible to finish the exam");
            return;
        }
        this.state = SessionState.FINISHED;
        for (Exam exam: exams.values().stream().filter(x -> !x.hasFinished()).collect(Collectors.toList()))
            finishExamStudent(exam);
        savingGrades();
    }

    @Override
    public boolean hasFinished() {
        return this.state == SessionState.FINISHED;
    }
}
