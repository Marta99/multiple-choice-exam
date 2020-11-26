package client;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Client {
    public static void main(String[] args) {
        String host = (args.length < 1) ? null : args[0];
        String sessionID = (args.length < 2) ? "SESSION1" : args[1];
        String studentID = (args.length < 3) ? "78099079A" : args[2];
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            MultipleChoiceClient client = new ClientMiddleware();
            MultipleChoiceServer stub = (MultipleChoiceServer) registry.lookup(sessionID);
            stub.register(client, studentID);
            System.out.println("Client registered, waiting for notification");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
