package common.data;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question implements Serializable {
    private final String question;
    private final List<Choice> choices;
    private final int correct;

    public Question(String question, List<Choice> choices, int correct) {
        this.question = question;
        this.choices = choices;
        //Check correct <= choices.size()
        this.correct = correct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return correct == question1.correct && question.equals(question1.question) && choices.equals(question1.choices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, choices, correct);
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", choices=" + choices +
                ", correct=" + correct +
                '}';
    }
}