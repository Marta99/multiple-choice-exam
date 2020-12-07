package server;

import common.MultipleChoiceServer;
import common.data.Question;
import org.apache.logging.log4j.LogManager;
import server.scanner.CommandScanner;
import server.scanner.CommandScannerInt;
import server.scanner.UnsupportedCommandException;
import server.session.Session;
import server.session.SessionException;

import java.io.*;
import java.nio.Buffer;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


public class Professor {

    private final BufferedWriter writer;
    private Session session;
    private final CommandScannerInt scanner;
    public static Logger logger = Logger.getLogger("LOGGER");

    public Professor(CommandScannerInt scanner, BufferedWriter gradeWriter) {
        this.scanner = scanner;
        this.writer = gradeWriter;
    }

    public void setSession(Session session) {
        this.session = session;
    }


    public void finishExam() throws IOException {
        logger.info("finish command");
        this.session.finishExam();
    }

    public void startExam() throws SessionException, IOException {
        logger.info("start command");
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


    public void receiveGrades(HashMap<String, Exam> exams) throws IOException {
        logger.info("Saving grades.");
        for (Map.Entry<String, Exam> entry : exams.entrySet()) {
            writer.write(entry.getKey() + "," + entry.getValue());
        }
        logger.info("Closing the grades file.");
        writer.close();
    }


    private static Registry startRegistry(Integer port) throws RemoteException {
        if (port == null) {
            port = 1099;
        }
        try {
            Registry registry = LocateRegistry.getRegistry(port);
            registry.list();
            // The above call will throw an exception
            // if the registry does not already exist
            return registry;
        } catch (RemoteException ex) {
            // No valid registry at that port.
            System.out.println("RMI registry cannot be located ");
            Registry registry = LocateRegistry.createRegistry(port);
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
        String pathExam = (args.length < 2) ? "./data/exam1.txt" : args[1];
        String pathGrades = (args.length < 3) ? "./data/grades1.txt" : args[1];

        //int numParticipants = (args.length < 2) ? 0 : Integer.parseInt(args[1]);
        try {
            Professor professor = new Professor(new CommandScanner(), new BufferedWriter(new FileWriter(pathGrades)));
            Session session = new Session(professor, sessionID, professor.loadQuestions(pathExam));
            bindingRegistry(sessionID, session);
            sessionFlow(professor, session);
        } catch (IOException | AlreadyBoundException e) {
            e.printStackTrace();
        }

    }
}
