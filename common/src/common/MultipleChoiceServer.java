package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MultipleChoiceServer extends Remote {
    void joinSession(int studentId) throws RemoteException;

    void receiveAnswer(int answerId) throws RemoteException;
}
