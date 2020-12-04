package client;

import common.MultipleChoiceClient;
import common.MultipleChoiceServer;
import common.data.Choice;
import common.data.Question;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.rmi.server.UnicastRemoteObject;
import java.util.List;


public class Client extends UnicastRemoteObject implements MultipleChoiceClient {
    private String studentID;

    public Client(String studentID) throws RemoteException {
        this.studentID = studentID;
    }

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        String sessionID = (args.length < 2) ? "SESSION1" : args[1];
        String studentID = (args.length < 3) ? "78099079A" : args[2];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            MultipleChoiceClient client = new Client(studentID);
            MultipleChoiceServer stub = (MultipleChoiceServer) registry.lookup(sessionID);
            stub.joinSession(client);
            System.out.println("You have joined the session, the exam is about to start.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receiveMSG(String s) throws RemoteException {
        System.out.println(s);
    }

    @Override
    public void receiveQuestion(Question question) throws RemoteException {

    }

    @Override
    public void receiveChoices(List<Choice> list) throws RemoteException {

    }

    @Override
    public void receiveGrade(int i) throws RemoteException {

    }

    @Override
    public void finishSessionStudent() throws RemoteException {

    }
}
