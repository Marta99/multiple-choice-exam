package server;

import common.MultipleChoiceClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExamTest {

    private Exam exam;
    private MultipleChoiceClient client;
    private List<QuestionAdapter> questions;

    @BeforeEach
    void setUp() throws IOException {
        CSVReader reader = new CSVReader(new BufferedReader(new FileReader("./data/exam1.txt")));
        client = Mockito.mock(MultipleChoiceClient.class);
        questions = reader.getQuestions();
        exam = new Exam(client, questions);
    }

    @Test
    void gettingGradeAfterFinishing() {
        assertThrows(UnsupportedOperationException.class, () -> exam.getGrade());
    }

    @Test
    void gettingLastQuestionBeforeNext() {
        assertNull(exam.getLastQuestion());
    }

    @Test
    void gettingNextQuestions() {
        for(var question: questions) {
            assertEquals(question, exam.next());
        }
        assertEquals(questions.get(questions.size() - 1), exam.getLastQuestion());
    }

    @Test
    void evaluatingNoLastQuestion() {
        assertThrows(UnsupportedOperationException.class, ()-> exam.evaluateLastQuestion(2));
    }

    @Test
    void evaluatingNoCorrectAnswer() {
        assertThrows(UnsupportedOperationException.class, ()-> exam.evaluateLastQuestion(-3));
        assertThrows(UnsupportedOperationException.class, ()-> exam.evaluateLastQuestion(34));
    }

    @Test
    void finishingBeforeAnswering() {
        exam.next();
        exam.evaluateLastQuestion(2);
        int grade = exam.finish();
        assertEquals(1, grade);
        assertTrue(exam.hasFinished());
        assertThrows(UnsupportedOperationException.class, () -> exam.next());
    }



}