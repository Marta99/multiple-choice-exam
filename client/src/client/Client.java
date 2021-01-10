package client;

import common.MultipleChoiceClient;
import common.MultipleChoiceServer;
import common.api.MyExams;
import common.api.data.LocationAPI;
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
import java.util.Scanner;


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

    public static void main(String[] args) throws InterruptedException, IOException {
        //String studentID = (args.length < 1) ? "78099079B" : args[0];
        System.out.print("Enter your student ID: ");
        Scanner scanner = new Scanner(System.in);
        String studentID = scanner.nextLine();
        int examID = (args.length < 1) ? 2 : Integer.parseInt(args[0]);
        System.out.println(examID);
        Optional<LocationAPI> location  = MyExams.verifyStudentID(examID, studentID);
        if (location.isEmpty()) {
            System.out.println("Not accepted ID.");
            System.exit(-1);
        }
        String sessionID = location.get().getBindKey();
        try {
            Registry registry = getRegistry(location);
            Client client = new Client(studentID, new AnswerScanner(), new Displayer());
            synchronized (client) {
                MultipleChoiceServer stub = (MultipleChoiceServer) registry.lookup(sessionID);
                client.joinSession(stub);
                client.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread.sleep(50);
        System.exit(0);
    }

    private static Registry getRegistry(Optional<LocationAPI> location) throws RemoteException {
        System.out.println(location.get().getHost());
        if (location.get().getHost() == "localhost")
            return LocateRegistry.getRegistry();
        return LocateRegistry.getRegistry(location.get().getHost(), location.get().getPort());
    }

    public void joinSession(MultipleChoiceServer session) throws RemoteException {
        this.session = session;
        String msg = session.joinSession((MultipleChoiceClient) this);
        displayer.display(msg);
        if (!msg.equals("You have joined the session")) {
            System.exit(0);
        }
    }


    @Override
    public void receiveMSG(String s) throws RemoteException {
        displayer.display(s);
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
