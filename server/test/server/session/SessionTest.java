package server.session;

import common.MultipleChoiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import server.CSVReader;
import server.Professor;
import server.QuestionAdapter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    private Session session;
    private Professor professorMock;
    private MultipleChoiceClient client;
    private List<QuestionAdapter> questions;

    @BeforeEach
    void setUp() throws IOException {
        professorMock = Mockito.mock(Professor.class);
        CSVReader reader = new CSVReader(new BufferedReader(new FileReader("./data/exam1.csv")));
        client = Mockito.mock(MultipleChoiceClient.class);
        Mockito.when(client.getUniversityID()).thenReturn("1234567890");
        questions = reader.getQuestions();
        session = new Session(professorMock, "1234", questions);
    }

    @Test
    void joinSessionWith1User() throws RemoteException {
        String expected = "You have joined the session";
        String msg = session.joinSession(client);
        Mockito.verify(client).getUniversityID();
        assertEquals(expected, msg);
    }


    @Test
    void joinSessionWith2UserCorrectly() throws RemoteException {
        String expected = "You have joined the session";
        String msg = session.joinSession(client);
        assertEquals(expected, msg);
        MultipleChoiceClient client2 = Mockito.mock(MultipleChoiceClient.class);
        Mockito.when(client2.getUniversityID()).thenReturn("0987654321");
        msg = session.joinSession(client2);
        assertEquals(expected, msg);
    }

    @Test
    void joinSessionTwice() throws RemoteException {
        String expected = "There is a student with tha same ID registered in the session. Try to reconnect with a different ID.";
        session.joinSession(client);
        assertEquals(expected, session.joinSession(client));
    }

    @Test
    void joinSessionWith2UsersWithSameID() throws RemoteException {
        String expected = "There is a student with tha same ID registered in the session. Try to reconnect with a different ID.";
        session.joinSession(client);
        MultipleChoiceClient client2 = Mockito.mock(MultipleChoiceClient.class);
        Mockito.when(client2.getUniversityID()).thenReturn("1234567890");
        String msg = session.joinSession(client2);
        assertEquals(expected, msg);
    }


    @Test
    void joinExamAfterStarting() throws IOException, SessionException {
        session.joinSession(client);
        session.startExam();
        MultipleChoiceClient client2 = Mockito.mock(MultipleChoiceClient.class);
        String expected = "The Exam has already started.";
        assertEquals(expected, session.joinSession(client2));
    }

    @Test
    void joinExamAfterFinishing() throws IOException, SessionException {
        session.joinSession(client);
        session.startExam();
        session.finishExam();
        session.joinSession(client);
        assertEquals("The Exam has already finished.", session.joinSession(client));
    }

    @Test
    void receiveAnswerBeforeStarting() throws Exception {
        session.joinSession(client);
        session.receiveAnswer(client, 2);
        Mockito.verify(client).receiveMSG("Sorry. I can't take into account your answer.");
    }

    @Test
    void receiveAnswerAfterFinishingSessionExam() throws Exception {
        session.joinSession(client);
        session.startExam();
        Thread.sleep(200);
        session.finishExam();
        Thread.sleep(200);
        Mockito.verify(client).receiveGrade(0, 5);
        session.receiveAnswer(client, 2);
        Mockito.verify(client).receiveMSG("Sorry. I can't take into account your answer.");
        Mockito.verify(client, Mockito.times(2)).receiveGrade(0, 5);
    }

    @Test
    void receivedAnswerAfterFinishing1Exam() throws Exception {
        session.joinSession(client);
        MultipleChoiceClient client2 = Mockito.mock(MultipleChoiceClient.class);
        session.joinSession(client2);
        session.startExam();
        for (var question : questions) {
            session.receiveAnswer(client, 2);
        }
        session.receiveAnswer(client, 2);
        Mockito.verify(client).receiveMSG("Your exam has finished.");
    }

    @Test
    void receivingBadAnswer() throws Exception {
        String msg = "Your answer is not properly suitable for that question. The question is:";
        session.joinSession(client);
        session.startExam();
        Thread.sleep(100);
        Mockito.verify(client, Mockito.times(1)).receiveQuestion(questions.get(0).getQuestion());
        session.receiveAnswer(client, -2);
        Mockito.verify(client, Mockito.times(1)).receiveMSG(msg);
        Mockito.verify(client, Mockito.times(2)).receiveQuestion(questions.get(0).getQuestion());
        session.receiveAnswer(client, 2032);
        Mockito.verify(client, Mockito.times(2)).receiveMSG(msg);
        Mockito.verify(client, Mockito.times(3)).receiveQuestion(questions.get(0).getQuestion());
    }


    @Test
    void startExamBeforeJoining() {
        assertThrows(SessionException.class, () -> session.startExam());
    }

    @Test
    void startExamWith1User() throws IOException, SessionException, InterruptedException {
        session.joinSession(client);
        session.startExam();
        var firstQuestion = questions.get(0).getQuestion();
        Thread.sleep(200);
        Mockito.verify(client).receiveQuestion(firstQuestion);
    }

    @Test
    void startExamWith2User() throws IOException, SessionException, InterruptedException {
        session.joinSession(client);
        MultipleChoiceClient client2 = Mockito.mock(MultipleChoiceClient.class);
        session.joinSession(client2);
        session.startExam();
        var firstQuestion = questions.get(0).getQuestion();
        Thread.sleep(200);
        Mockito.verify(client).receiveQuestion(firstQuestion);
        Mockito.verify(client2).receiveQuestion(firstQuestion);
    }

    @Test
    void startExamAfterFinishing() throws IOException, SessionException {
        session.joinSession(client);
        session.startExam();
        assertThrows(UnsupportedSessionStateException.class, () -> session.startExam());
    }

    @Test
    void finishingCorrectlyExam() throws IOException, SessionException {
        session.joinSession(client);
        session.startExam();
        session.finishExam();
        assertTrue(session.hasFinished());
    }

    @Test
    void finishingExamBeforeStarting() throws IOException {
        session.joinSession(client);
        session.finishExam();
        Mockito.verify(professorMock).receiveMSG("It is not possible to finish the exam, it has not been started already.");
    }
}