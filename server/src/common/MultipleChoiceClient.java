package common;

import common.data.Choice;
import common.data.Question;
import server.QuestionAdapter;

import java.io.IOException;
import java.lang.reflect.InaccessibleObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface MultipleChoiceClient extends Remote {

    void receiveMSG(String msg) throws RemoteException;

    void receiveQuestion(Question question) throws IOException;

    void receiveGrade(int grade) throws RemoteException;

    void finishSessionStudent() throws RemoteException;

    String getUniversityID() throws RemoteException;
}