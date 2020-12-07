package server;

import common.MultipleChoiceServer;
import common.data.Question;
import server.scanner.CommandScanner;
import server.scanner.CommandScannerInt;
import server.scanner.UnsupportedCommandException;
import server.session.Session;
import server.session.SessionException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;


public class Professor {

    private Session session;
    private CommandScannerInt scanner;

    public Professor(CommandScannerInt scanner) {
        this.scanner = scanner;
    }

    public void setSession(Session session) {
        this.session = session;
    }


    public void finishExam() {
        this.session.finishExam();
    }

    public void startExam() throws SessionException, IOException {
        session.startExam();
    }

    public void receiveMSG(String msg) {
        System.out.println("[PROFESSOR]: " + msg);
    }


    private void scan() throws IOException, UnsupportedCommandException, SessionException {
        //TODO: We could do it with hashamp command-runnable but it is not worth it.
        switch (this.scanner.scan()) {
            case START_EXAM:
                this.startExam();
                break;
            case FINISH_EXAM:
                this.finishExam();
                break;
        }
    }

    public List<QuestionAdapter> loadQuestions(String path) throws IOException {
        CSVReader reader = new CSVReader(new BufferedReader(new FileReader(path)));
        return reader.getQuestions();
    }



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

    private static void bindingRegistry(String sessionID, MultipleChoiceServer session) throws RemoteException, AlreadyBoundException {
        Registry registry = startRegistry(null);
        registry.bind(sessionID, session);
        System.err.println("Server ready. register clients and notify each 5 seconds");
    }


    private static void sessionFlow(Professor professor, Session session) throws IOException {
        while (!session.hasFinished()) {
            try {
                System.out.print("> ");
                professor.scan();
            } catch (SessionException | UnsupportedCommandException e) {
                System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        String sessionID = (args.length < 1) ? "SESSION1" : args[0];
        String pathExam = (args.length < 1) ? "./data/exam1.txt" : args[1];
        //int numParticipants = (args.length < 2) ? 0 : Integer.parseInt(args[1]);
        try {
            Professor professor = new Professor(new CommandScanner());
            Session session = new Session(professor, sessionID, professor.loadQuestions(pathExam));
            bindingRegistry(sessionID, session);
            sessionFlow(professor, session);
        } catch (IOException | AlreadyBoundException e) {
            e.printStackTrace();
        }

    }


}
