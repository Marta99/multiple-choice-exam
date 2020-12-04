package common;

import common.data.Choice;
import common.data.Question;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface MultipleChoiceClient extends Remote {

    void joinSession() throws RemoteException;

    void receiveQuestion(Question question) throws RemoteException;

    void receiveChoices(List<Choice> choices) throws RemoteException;

    void receiveGrade(int grade) throws RemoteException;

    void finishSessionStudent() throws RemoteException;

}