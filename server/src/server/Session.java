package server;

import common.MultipleChoiceClient;
import common.MultipleChoiceServer;
import common.data.Exam;
import common.data.Question;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Session extends UnicastRemoteObject implements MultipleChoiceServer, ExamController {

    private final ArrayList<MultipleChoiceClient> clients = new ArrayList<MultipleChoiceClient>();
    private final String sessionID;
    private final List<Question> questions;


    public Session(String sessionID, List<Question> questions) throws RemoteException {
        this.sessionID = sessionID;
        this.questions = questions;

    }

    @Override
    public void joinSession(MultipleChoiceClient client) throws RemoteException {
        this.clients.add(client);
        System.out.println("A student has joined the session " + sessionID + ". Now there are " + this.clients.size() +
                " students in the session.");
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
    public void startExam() {
        for (MultipleChoiceClient c : clients) {

        }
    }

    @Override
    public void finishExam() {

    }
}
