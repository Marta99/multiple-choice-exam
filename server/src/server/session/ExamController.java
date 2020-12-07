package server.session;

import java.rmi.RemoteException;

public interface ExamController {

    public void startExam() throws UnsupportedSessionStateException, SessionException, RemoteException;

    public void finishExam();

    public boolean hasFinished();
}
