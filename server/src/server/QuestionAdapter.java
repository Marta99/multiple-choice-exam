package server;

import common.data.Choice;
import common.data.Question;
import common.data.QuestionInt;

import java.util.List;

public class QuestionAdapter implements QuestionInt {

    private final Question question;
    private final int correctAnswer;

    public QuestionAdapter(Question question, int i) {
        this.question = question;
        //Check correct <= choices.size()
        this.correctAnswer = i;
    }

    public boolean evaluate(int i) {
        return this.correctAnswer == i;
    }

    public Question getQuestion() {
        return question;
    }

    @Override
    public int numAnswers() {
        return question.numAnswers();
    }

    @Override
    public String getQuestionTitle() {
        return question.getQuestionTitle();
    }

    @Override
    public List<Choice> getChoices() {
        return question.getChoices();
    }
}
