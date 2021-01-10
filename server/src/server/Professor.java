package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.MultipleChoiceServer;
import common.api.MyExams;
import common.api.data.*;
import server.data.ExamInfo;
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
import java.util.stream.Collectors;


public class Professor {

    private final String pathGrade;
    private Session session;
    public static Logger logger = Logger.getLogger("LOGGER");
    private static Command lastCommand;
    private ExamAPI examAPI;

    public Professor(String pathGrade) {
        this.pathGrade = pathGrade;
    }

    public void setSession(Session session) {
        this.session = session;
    }


    public void finishExam() throws IOException, InterruptedException {
        this.session.finishExam();
    }

    public void startExam() throws SessionException, IOException {
        session.startExam();
    }

    public void receiveMSG(String msg) {
        System.out.println("[PROFESSOR]: " + msg);
    }


    public List<QuestionAdapter> loadQuestions(String path) throws IOException {
        CSVReader reader = new CSVReader(new BufferedReader(new FileReader(path)));
        return reader.getQuestions();
    }


    public void receiveGrades(HashMap<String, Exam> exams) throws IOException, InterruptedException {
        logger.info("Saving grades...");
        BufferedWriter writer = new BufferedWriter(new FileWriter(pathGrade));
        for (Map.Entry<String, Exam> entry : exams.entrySet()) {
            writer.write(entry.getKey() + "," + entry.getValue().getGrade() + "/" + entry.getValue().getNumQuestions() + '\n');
            MyExams.storeGrades(
                    this.examAPI.getId(),
                    entry.getKey(),
                    new GradeAPI(entry.getValue().getGrade(), new StudentAPI(entry.getKey())));
        }
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
            System.out.println("RMI registry created at port " + port);
            return registry;
        }
    }

    private static void bindingRegistry(String sessionID, MultipleChoiceServer session, int port) throws RemoteException, AlreadyBoundException {
        Registry registry = startRegistry(port);
        registry.bind(sessionID, session);
        System.err.println("Server ready. Waiting for students to join the session.");
    }


    private static void sessionFlow(Professor professor, Session session) throws IOException {
        new ScannerThread(session, new CommandScanner()).start();
        synchronized (session) {
            while (!session.hasFinished()) {
                try {
                    System.out.print("> ");
                    session.wait();
                    if (lastCommand == Command.START_EXAM) {
                        professor.startExam();
                    } else if (lastCommand == Command.FINISH_EXAM) {
                        professor.finishExam();
                    }
                    lastCommand = null;
                } catch (InterruptedException | SessionException e) {
                    System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        String pathInfoExam = (args.length < 1) ? "./data/exam.json" : args[1];
        String pathExam = (args.length < 2) ? "./data/exam1.csv" : args[2];
        String pathGrades = (args.length < 3) ? "./data/grades1.csv" : args[3];
        ExamInfo exam = new ObjectMapper().readValue(new File(pathInfoExam), ExamInfo.class);
        LocationAPI location = exam.getLocation();
        String sessionID = location.getBindKey();
        System.out.println(sessionID);
        try {
            Professor professor = new Professor(pathGrades);
            List<QuestionAdapter> questions = professor.loadQuestions(pathExam);
            POSTExamAPI postExamAPI = POSTExamAPI.fromExamInfo(
                    exam,
                    questions.stream().map(QuestionAdapter::toQuestionAPI)
                            .collect(Collectors.toList()));
            Optional<ExamAPI> examAPI = MyExams.postExam(postExamAPI);
            if (examAPI.isEmpty()) {
                System.out.println("Exam could not be uploaded");
                System.exit(-1);
            }
            System.out.println("Exam id: " + examAPI.get().getId());
            professor.setExamAPI(examAPI.get());
            Session session = new Session(professor, sessionID, questions);
            bindingRegistry(sessionID, session, location.getPort());
            System.out.println("In order to start the exam is needed to write 'start' in the terminal");
            System.out.println("In order to finish the exam is needed to write 'finish' in the terminal");
            sessionFlow(professor, session);
        } catch (IOException | AlreadyBoundException | InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);

    }

    private void setExamAPI(ExamAPI examAPI) {
        this.examAPI = examAPI;
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
