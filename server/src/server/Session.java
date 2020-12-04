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
        System.out.println("Accepted server");

    }

    @Override
    public void receiveAnswer(int i) throws RemoteException {

    }
}
