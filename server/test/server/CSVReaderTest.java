package server;

import common.data.Choice;
import common.data.Question;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CSVReaderTest {

    static String csv;
    static ArrayList<String> questions;
    private CSVReader reader;

    static class BufferedMock extends BufferedReader {
        private int i;
        private List<String> questionsReader;

        public BufferedMock(Reader in, int sz, List<String> questions) {
            super(in, sz);
            this.questionsReader = questions;
            i = 0;
        }

        public void setI(int i) {
            this.i = i;
        }

        public void setQuestionsReader(List<String> questionsReader) {
            this.questionsReader = questionsReader;
        }

        @Override
        public String readLine() throws IOException {
            if (i >= questionsReader.size())
                return null;
            String question = questionsReader.get(i);
            i++;
            return question;
        }
    }

    @BeforeAll
    static void setUpAll() {
        int NUM_QUESTIONS = 5;
        int NUM_ANSWERS = 4;
        StringBuilder builder = new StringBuilder();
        questions = new ArrayList<>();
        for (int i = 0; i < NUM_QUESTIONS; i++) {
            StringBuilder questionBuilder = new StringBuilder()
                    .append("Question").append(i + 1).append(";");
            for (int j = 0; j < NUM_ANSWERS; j++)
                questionBuilder.append("Answer").append(j + 1).append(";");
            questionBuilder.append(2).append('\n');
            builder.append(questionBuilder);
            questions.add(questionBuilder.toString());
        }
        csv = builder.toString();
    }

    @BeforeEach
    void setUp() throws IOException {
        Reader r = Mockito.mock(Reader.class);
        BufferedMock mock = new BufferedMock(r, 2, questions);
        reader = new CSVReader(mock);
    }

    @Test
    void addQuestion() {
        Question question = CSVReader.addQuestion(questions.get(0));
        List<String> answers = List.of("Answer1", "Answer2", "Answer3", "Answer4");
        Question expected = createQuestion(1, answers);
        assertEquals(expected, question);
    }

    private Question createQuestion(int iQuestion, List<String> answers) {
        return new Question("Question" + iQuestion, answers.stream()
                .map(x -> {
                    String value = String.valueOf(x.charAt(x.length() - 1));
                    int i = Integer.parseInt(value);
                    return new Choice(i, x);
                }).collect(Collectors.toList()), 2);
    }

    @Test
    void readFile() {
        List<String> answers = List.of("Answer1", "Answer2", "Answer3", "Answer4");
        List<Question> currentQuestions  = reader.getQuestions();
        for(int i=0; i < currentQuestions.size(); i++) {
            assertEquals(createQuestion(i+1, answers), currentQuestions.get(i));
        }
    }
}