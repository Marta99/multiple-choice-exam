package server;

import common.api.data.QuestionAPI;
import common.data.Choice;
import common.data.Question;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class QuestionAdapter {

    private final int correctAnswer;
    private final Question question;

    public QuestionAdapter(Question q, int correctAnswer) {
        this.question = q;
        if (!(0 < correctAnswer && correctAnswer <= q.numAnswers()))
            throw new UnsupportedOperationException("Correct answer must be between 1 and " + q.numAnswers());
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

    public QuestionAPI toQuestionAPI() {
        return new QuestionAPI(
                question.getQuestionTitle(),
                question.getChoices()
                        .stream().map(Choice::toChoiceAPI)
                        .collect(Collectors.toList()),
                correctAnswer);
    }
}
