package server;

import common.data.Choice;
import common.data.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVReader {

    private final List<QuestionAdapter> questions;

    public CSVReader(BufferedReader reader) throws IOException {
        this.questions = new ArrayList<>();
        String line = reader.readLine();
        while (line != null) {
            this.questions.add(addQuestion(line));
            line = reader.readLine();
        }
    }

    protected static QuestionAdapter addQuestion(String line) {
        List<String> words = Arrays.asList(line.split("[[;]['\n]]"));
        String question = words.get(0);
        List<String> strChoices = words.subList(1, words.size() - 1);
        List<Choice> choices = getChoices(strChoices);
        String lastWord = words.get(words.size() - 1);
        int correct = Integer.parseInt(lastWord.split("[[.]]")[0]);
        return new QuestionAdapter(new Question(question, choices), correct);
    }

    protected static List<Choice> getChoices(List<String> strChoices) {
        List<Choice> choices = new ArrayList<>();
        for (int i = 0; i < strChoices.size(); i++) {
            choices.add(new Choice(i+1, strChoices.get(i)));
        }
        return choices;
    }

    public List<QuestionAdapter> getQuestions() {
        return questions;
    }
}
