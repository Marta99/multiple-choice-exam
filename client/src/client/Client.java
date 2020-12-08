package client;

import common.MultipleChoiceClient;
import common.MultipleChoiceServer;
import common.data.Choice;
import common.data.Question;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Optional;


public class Client extends UnicastRemoteObject implements MultipleChoiceClient {
    private final AnswerScannerInt<Integer> scanner;
    private final DisplayerInt displayer;
    private String studentID;
    private MultipleChoiceServer session;

    public Client(String studentID, AnswerScannerInt<Integer> scanner, DisplayerInt displayer) throws RemoteException {
        super();
        this.studentID = studentID;
        this.scanner = scanner;
        this.displayer = displayer;
    }

    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        String sessionID = (args.length < 2) ? "SESSION1" : args[1];
        String studentID = (args.length < 3) ? "78099079A" : args[2];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            Client client = new Client(studentID, new AnswerScanner(), new Displayer());
            synchronized (client) {
                MultipleChoiceServer stub = (MultipleChoiceServer) registry.lookup(sessionID);
                client.joinSession(stub);
                client.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void joinSession(MultipleChoiceServer session) throws RemoteException {
        this.session = session;
        String msg = session.joinSession((MultipleChoiceClient) this);
        System.out.println(msg);
        if (!msg.equals("You have joined the session")) {
            System.exit(0);
        }
    }


    @Override
    public void receiveMSG(String s) throws RemoteException {
        System.out.println(s);
    }

    @Override
    public void receiveQuestion(Question question) throws Exception {
        displayer.display(question.getQuestionTitle());
        for (var choice: question.getChoices()) {
            displayer.display(choice.getChoice());
        }
        Optional<Integer> optAnswer = scanner.scanAnswerID();
        while(optAnswer.isEmpty()) {
            displayer.display("Answer not supported");
            optAnswer = scanner.scanAnswerID();
        }
        session.receiveAnswer(this, optAnswer.get());
    }

    @Override
    public void receiveGrade(int grade, int numQuestions) throws RemoteException {
        displayer.display("You have finished the exam!");
        displayer.display("Your grade is: " + grade + "/" + numQuestions);
        synchronized (this){
            this.notify();
        }
    }

    @Override
    public void finishSessionStudent() throws RemoteException {
        System.exit(1);
    }

    @Override
    public String getUniversityID() throws RemoteException {
        return this.studentID;
    }

    public void display(String msg) {
        this.displayer.display(msg);
    }


}
