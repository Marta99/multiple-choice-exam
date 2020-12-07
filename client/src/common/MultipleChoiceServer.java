
package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MultipleChoiceServer extends Remote {

    String joinSession(MultipleChoiceClient client) throws RemoteException;

    void receiveAnswer(MultipleChoiceClient c, int i) throws Exception;
}
