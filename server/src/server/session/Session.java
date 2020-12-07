package server.session;

import common.MultipleChoiceClient;
import common.MultipleChoiceServer;
import common.data.Exam;
import common.data.Question;
import server.Professor;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Session extends UnicastRemoteObject implements MultipleChoiceServer, ExamController {

    private final HashMap<String, MultipleChoiceClient> clients = new HashMap<>();
    private final String sessionID;
    private final List<Question> questions;
    private final Professor professor;
    private SessionState state;
    private final HashMap<String, Exam> exams;



    public Session(Professor professor, String sessionID, List<Question> questions) throws RemoteException {
        this.sessionID = sessionID;
        this.professor = professor;
        this.questions = questions;
        this.exams = new HashMap<>();
        this.professor.setSession(this);
        this.state = SessionState.OPENED;
    }

    @Override
    public void joinSession(MultipleChoiceClient client) throws RemoteException {
        if (this.state != SessionState.OPENED) {
            //TODO: Do it better from client part and here
            System.out.println("CLIENT REJECTED");
            client.receiveMSG("CLIENT REJECTED FROM SERVER");
            return;
        }
        this.clients.put(client.getUniversityID(), client);
        professor.receiveMSG("A student has joined the session " + sessionID + ".");
        professor.receiveMSG("Now there are" + this.clients.size() +"students in the session.");
    }

    @Override
    public void receiveAnswer(int i) throws RemoteException {

    }

    public void notifyClients() {
        for (int i = 0; i < this.clients.size(); i++) {
            try {
                this.clients.get(i).receiveMSG("The exam starts!");
                //this.clients.get(i).receiveQuestion();
                //this.clients.get(i).receiveChoices();
            } catch (RemoteException e) {
                System.out.println("error in call");
                this.clients.remove(i);
                System.out.println("A student leaved the session. There are" + this.clients.size() + " students in the session");
            }
        }
    }



    @Override
    public void startExam() throws SessionException, RemoteException {
        if (this.state != SessionState.OPENED)
            throw new UnsupportedSessionStateException("Session is not opened");
        if (clients.size() == 0)
            throw new SessionException("There are no students in the session to start the session");
        this.state = SessionState.STARTED;
        clients.forEach((s, c) -> exams.put(s, new Exam(c, questions)));
        for(var e: exams.values())
            e.getStudent().receiveQuestion(e.next());
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
