package server.session;

import common.MultipleChoiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import server.CSVReader;
import server.Professor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    private Session session;
    private Professor professorMock;
    private MultipleChoiceClient client;

    @BeforeEach
    void setUp() throws IOException {
        professorMock = Mockito.mock(Professor.class);
        CSVReader reader = new CSVReader(new BufferedReader(new FileReader("./data/exam1.txt")));
        client = Mockito.mock(MultipleChoiceClient.class);
        Mockito.when(client.getUniversityID()).thenReturn("1234567890");
        session = new Session(professorMock, "1234", reader.getQuestions());
    }

    @Test
    void joinSessionWith1User() throws RemoteException {
        String expected = "You have joined the session";
        String msg = session.joinSession(client);
        assertEquals(expected, msg);
    }


    @Test
    void joinSessionWith2User() throws RemoteException {
        String expected = "You have joined the session";
        String msg = session.joinSession(client);
        assertEquals(expected, msg);
        MultipleChoiceClient client2 = Mockito.mock(MultipleChoiceClient.class);
        Mockito.when(client2.getUniversityID()).thenReturn("0987654321");
        msg = session.joinSession(client2);
        assertEquals(expected, msg);
    }

    @Test
    void receiveAnswer() {

    }
}