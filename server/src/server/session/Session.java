package server.session;

import common.MultipleChoiceClient;
import common.MultipleChoiceServer;
import common.data.Exam;
import common.data.Question;
import server.Professor;
import server.QuestionAdapter;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
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
        if (this.state != SessionState.OPENED) {
            //TODO: Do it better from client part and here
            System.out.println("CLIENT REJECTED");
            return "CLIENT REJECTED FROM SERVER";
        }
        this.clients.put(client.getUniversityID(), client);
        professor.receiveMSG("A student has joined the session " + sessionID + ".");
        professor.receiveMSG("Now there are " + this.clients.size() +" students in the session.");
        return "CLIENT HAS JOINED THE SESSION";
    }

    @Override
    public void receiveAnswer(MultipleChoiceClient c, int i) throws Exception {
        Exam exam = this.exams.get(c.getUniversityID());
        if (!(1 <= i && i < exam.getLastQuestion().numAnswers()))
            System.out.println("Answer not received well");
        exam.evaluateLastQuestion(i);
        if (exam.hasNext())
            exam.next();
        c.finishSessionStudent();
    }

    @Override
    public void startExam() throws SessionException, IOException {
        if (this.state != SessionState.OPENED)
            throw new UnsupportedSessionStateException("Session is not opened");
        if (clients.size() == 0)
            throw new SessionException("There are no students in the session to start the session");
        this.state = SessionState.STARTED;
        clients.forEach((s, c) -> exams.put(s, new Exam(c, questions)));
        for(var e: exams.values()) {
            QuestionAdapter q = e.next();
            e.getStudent().receiveQuestion(q.getQuestion());
        }
    }

    @Override
    public void finishExam() {
        if (this.state != SessionState.STARTED)
            //TODO: Not able to blabalbal
            return;
        this.state = SessionState.FINISHED;
    }

    @Override
    public boolean hasFinished() {
        return this.state == SessionState.FINISHED;
    }
}
