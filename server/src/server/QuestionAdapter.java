package server;

import common.data.Choice;
import common.data.Question;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class QuestionAdapter {

    private final int correctAnswer;
    private final Question question;

    public QuestionAdapter(Question q, int correctAnswer) {
        this.question = q;
        //Check correct <= choices.size()
        this.correctAnswer = correctAnswer;
    }

    public boolean evaluate(int i) {
        return this.correctAnswer == i;
    }

    public Question getQuestion() {
        return question;
    }


    public int numAnswers() {
        return question.numAnswers();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QuestionAdapter that = (QuestionAdapter) o;
        return correctAnswer == that.correctAnswer && Objects.equals(question, that.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(correctAnswer, question);
    }
}
