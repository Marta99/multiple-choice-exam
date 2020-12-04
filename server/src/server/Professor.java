package src.server;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Professor {

    private static Registry startRegistry(Integer port) throws RemoteException {
        if(port == null) {
            port = 1099;
        }
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.list( );
            // The above call will throw an exception
            // if the registry does not already exist
            return registry;
        } catch (RemoteException ex) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located ");
            Registry registry= LocateRegistry.createRegistry(port);
            System.out.println("RMI registry created at port ");
            return registry;
        }
    }

    public static void main(String[] args) {
        String sessionID = (args.length < 1) ? "SESSION1" : args[0];
        int numParticipants = (args.length < 2) ? 1 : Integer.parseInt(args[1]);
        try {
            Registry registry = startRegistry(null);
            Session session = new Session(sessionID, numParticipants);
            registry.bind(sessionID, (MultipleChoiceServer) session);
            System.err.println("Server ready. register clients and notify each 5 seconds");
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }

    }
}