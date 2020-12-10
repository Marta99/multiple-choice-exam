package client;

import common.MultipleChoiceServer;
import common.data.Choice;
import common.data.Question;
import org.mockito.Mockito;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ClientTest {

    Client client;
    AnswerScannerInt<Integer> scanner;
    MultipleChoiceServer server;
    DisplayerInt displayer;

    @BeforeEach
    void setUp() throws RemoteException {
        scanner = Mockito.mock(AnswerScanner.class);
        server = Mockito.mock(MultipleChoiceServer.class);
        displayer = Mockito.mock(Displayer.class);
        String studentID = "123456ASDF32";
        client = new Client(studentID, scanner, displayer);

    }

    @Test
    void joinSession() throws RemoteException {
        String msg = "You have joined the session";
        Mockito.when(server.joinSession(client)).thenReturn(msg);
        client.joinSession(server);
        Mockito.verify(displayer).display(msg);
    }

    @Test
    void receiveMSG() {
        String msg = "Mock Message";
        client.display(msg);
        Mockito.verify(displayer).display(msg);
    }

    @Test
    void receiveQuestion() throws Exception {
        int i = 1;
        ArrayList<Choice> choices = new ArrayList<>();
        List<String> choicesString = List.of("Choice1", "Choice2", "Choice3", "Choice4");
        for (var choice : choicesString) {
            choices.add(new Choice(i, choice));
            ++i;
        }
        Question question = new Question("QuestionTitle", choices);
        Mockito.when(scanner.scanAnswerID()).thenReturn(Optional.of(2));
        client.receiveQuestion(question);

    }

    @Test
    void receiveGrade() {
    }

    @Test
    void finishSessionStudent() {
    }

    @Test
    void getUniversityID() {
    }
}