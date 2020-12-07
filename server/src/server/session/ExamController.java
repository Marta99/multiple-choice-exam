package server.session;

import java.io.IOException;
import java.rmi.RemoteException;

public interface ExamController {

    public void startExam() throws UnsupportedSessionStateException, SessionException, IOException;

    public void finishExam();

    public boolean hasFinished();
}
