package server;

import common.data.Choice;
import common.data.Question;

import java.io.Serializable;
import java.util.List;

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
}
