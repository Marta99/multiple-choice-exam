package server;

import common.MultipleChoiceClient;
import common.MultipleChoiceServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class Session extends UnicastRemoteObject implements MultipleChoiceServer {

    private ArrayList<MultipleChoiceClient> clients = new ArrayList<MultipleChoiceClient>();
    private String sessionID;
    //private int numParticipants;


    public Session(String sessionID) throws RemoteException {
        this.sessionID = sessionID;
        //this.numParticipants = numParticipants;
    }

    @Override
    public void joinSession(MultipleChoiceClient client) throws RemoteException {
        this.clients.add(client);
        System.out.println("A student has joined the session. Now there are " + this.clients.size() + " students in the session.");

    }

    @Override
    public void receiveAnswer(int i) throws RemoteException {

    }

    public void notifyClients(){
        for (int i=0; i< this.clients.size(); i++){
            try {
                this.clients.get(i).receiveMSG("The exam starts!");
                //this.clients.get(i).receiveQuestion();
                //this.clients.get(i).receiveChoices();
            }catch(RemoteException e){
                System.out.println("error in call");
                this.clients.remove(i);
                System.out.println("A student leaved the session. There are" + this.clients.size() + " students in the session");
            }
        }
    }

}
