package src.server;

import common.MultipleChoiceServer;

import java.rmi.RemoteException;

public class Session implements MultipleChoiceServer {

    private String sessionID;
    private int numParticipants;

    public Session(String sessionID, int numParticipants) {
        this.sessionID = sessionID;
        this.numParticipants = numParticipants;
    }

    @Override
    public void joinSession(int i) throws RemoteException {

    }

    @Override
    public void receiveAnswer(int i) throws RemoteException {

    }
}
