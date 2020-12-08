package common.data;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Question implements Serializable {
    private final String question;
    private final List<Choice> choices;

    public Question(String question, List<Choice> choices) {
        this.question = question;
        this.choices = choices;
    }

    public int numAnswers() {
        return this.choices.size();
    }

    public String getQuestionTitle() {
        return this.question;
    }

    public List<Choice> getChoices() {
        return List.copyOf(this.choices);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question1 = (Question) o;
        return question.equals(question1.question) && choices.equals(question1.choices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, choices);
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", choices=" + choices +
                '}';
    }
}