package server;

import common.data.Choice;
import common.data.Question;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class QuestionAdapterTest {

    private QuestionAdapter questionAdapter;
    private Question question;

    @BeforeEach
    void setUp() {
        question = new Question("QuestionTitle", List.of(new Choice(1, "Answer1"), new Choice(2, "Answer2")));
        questionAdapter = new QuestionAdapter(question, 2);
    }

    @Test
    void evaluate() {
        assertTrue(questionAdapter.evaluate(2));
        assertFalse(questionAdapter.evaluate(1));
        assertFalse(questionAdapter.evaluate(-1));
        assertFalse(questionAdapter.evaluate(200));
    }

    @Test
    void getQuestion() {
        assertEquals(question, questionAdapter.getQuestion());
    }

    @Test
    void numAnswers() {
        assertEquals(2, questionAdapter.numAnswers());
    }
}