package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MultipleChoiceServer extends Remote {
    void joinSession(int studentId) throws RemoteException;

    void receiveQuestion(int answerId) throws RemoteException;
}
