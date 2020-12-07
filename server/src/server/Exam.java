package server;

import common.MultipleChoiceClient;
import server.QuestionAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Exam implements Iterator<QuestionAdapter> {
    private final Iterator<QuestionAdapter> iterator;
    private final MultipleChoiceClient student;
    private boolean finished;
    private QuestionAdapter last;
    private int grade;

    public Exam (MultipleChoiceClient c, List<QuestionAdapter> questionsList) {
        this.student = c;
        ArrayList<QuestionAdapter> questions = new ArrayList<QuestionAdapter>(questionsList);
        finished = false;
        this.iterator = questions.iterator();
        grade = 0;
    }

    public MultipleChoiceClient getStudent() {
        return student;
    }

    public int getGrade() {
        if (!hasFinished())
            throw new UnsupportedOperationException("The grade can not be until the exam finishes.");
        return grade;
    }

    @Override
    public boolean hasNext() {
        boolean hasNext = this.iterator.hasNext();
        if (!hasNext && !finished)
            finished = true;
        return hasNext;
    }

    @Override
    public QuestionAdapter next() {
        this.last = this.iterator.next();
        return last;
    }

    public QuestionAdapter getLastQuestion() {
        return last;
    }

    public void evaluateLastQuestion(int i) throws Exception {
        QuestionAdapter last = this.getLastQuestion();
        if (!(1 <= i && i < last.numAnswers()))
            //TODO: Define a better exception
            throw new Exception("Not correct index");
        if (last.evaluate(i))
            grade++;
    }

    public int finish() {
        if (finished) {
            throw new UnsupportedOperationException("The exam has finished");
        }
        finished = true;
        return this.grade;
    }

    public boolean hasFinished() {
        return this.finished;
    }
}
