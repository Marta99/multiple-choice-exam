package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MultipleChoiceServer extends Remote {

    void joinSession(MultipleChoiceClient client) throws RemoteException;

    void receiveAnswer(int answerId) throws RemoteException;

}