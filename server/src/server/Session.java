package server;

import common.MultipleChoiceClient;
import common.MultipleChoiceServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Session extends UnicastRemoteObject implements MultipleChoiceServer {

    private String sessionID;
    private int numParticipants;

    public Session(String sessionID, int numParticipants) throws RemoteException {
        this.sessionID = sessionID;
        this.numParticipants = numParticipants;
    }

    @Override
    public void joinSession(MultipleChoiceClient multipleChoiceClient) throws RemoteException {
        System.out.println("A student has joined the session. Now there are " + numParticipants + " students in the session.");

    }

    @Override
    public void receiveAnswer(int i) throws RemoteException {

    }
}
