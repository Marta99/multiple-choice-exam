package server;

import common.MultipleChoiceServer;
import server.scanner.Command;
import server.scanner.CommandScanner;
import server.scanner.CommandScannerInt;
import server.scanner.UnsupportedCommandException;
import server.session.Session;
import server.session.SessionException;

import java.io.*;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;


public class Professor {

    private final String pathGrade;
    private Session session;
    public static Logger logger = Logger.getLogger("LOGGER");
    private static Command lastCommand;

    public Professor(String pathGrade) {
        this.pathGrade = pathGrade;
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


    public List<QuestionAdapter> loadQuestions(String path) throws IOException {
        CSVReader reader = new CSVReader(new BufferedReader(new FileReader(path)));
        return reader.getQuestions();
    }


    public void receiveGrades(HashMap<String, Exam> exams) throws IOException {
        logger.info("Saving grades.");
        BufferedWriter writer = new BufferedWriter(new FileWriter(pathGrade));
        for (Map.Entry<String, Exam> entry : exams.entrySet()) {
            writer.write(entry.getKey() + "," + entry.getValue().getGrade() + '\n');
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
        new ScannerThread(session, new CommandScanner()).start();
        synchronized (session) {
            while (!session.hasFinished()) {
                logger.info("At the beginning of the loop.");
                try {
                    System.out.print("> ");
                    session.wait();
                    if (lastCommand == Command.START_EXAM) {
                        professor.startExam();
                    } else if (lastCommand == Command.FINISH_EXAM){
                        professor.finishExam();
                    }
                    lastCommand = null;
                    logger.info("In the end of the loop.");
                    logger.info("Session: " + session.hasFinished());
                } catch (InterruptedException | SessionException e) {
                    System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        String sessionID = (args.length < 1) ? "SESSION1" : args[0];
        String pathExam = (args.length < 2) ? "./data/exam1.csv" : args[1];
        String pathGrades = (args.length < 3) ? "./data/grades1.csv" : args[1];
        try {
            Professor professor = new Professor(pathGrades);
            Session session = new Session(professor, sessionID, professor.loadQuestions(pathExam));
            bindingRegistry(sessionID, session);
            Professor.logger.info("In order to start the exam is needed to write 'start' in the terminal");
            Professor.logger.info("In order to finish the exam is needed to write 'finish' in the terminal");
            sessionFlow(professor, session);
        } catch (IOException | AlreadyBoundException e) {
            e.printStackTrace();
        }
        System.exit(0);

    }

    private static class ScannerThread extends Thread {

        private final CommandScannerInt scanner;
        private final Session semaphore;

        public ScannerThread(Session semaphore, CommandScannerInt scanner) {
            this.scanner = scanner;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            Optional<Command> command;
            while (true) {
                try {
                    command = Optional.of(scanner.scan());
                } catch (IOException e) {
                    command = Optional.empty();
                } catch (UnsupportedCommandException e) {
                    System.err.println(e.getMessage());
                    command = Optional.empty();
                }
                if (command.isPresent()) {
                    synchronized (semaphore) {
                        lastCommand = command.get();
                        semaphore.notify();
                    }
                }
            }
        }
    }
}
