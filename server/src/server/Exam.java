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
    private int numQuestions;

    public Exam (MultipleChoiceClient c, List<QuestionAdapter> questionsList) {
        this.student = c;
        ArrayList<QuestionAdapter> questions = new ArrayList<>(questionsList);
        finished = false;
        this.iterator = questions.iterator();
        grade = 0;
        numQuestions = questions.size();
    }

    public MultipleChoiceClient getStudent() {
        return student;
    }

    public int getGrade() {
        if (!hasFinished())
            throw new UnsupportedOperationException("The grade can not be until the exam finishes.");
        return grade;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    @Override
    public boolean hasNext() {
        boolean hasNext = this.iterator.hasNext();
        if (hasFinished())
            return false;
        if (!hasNext && !finished)
            finished = true;
        return hasNext;
    }

    @Override
    public QuestionAdapter next() {
        if (hasFinished())
            throw new UnsupportedOperationException("It cannot be possible to send the next question.");
        this.last = this.iterator.next();
        return last;
    }

    public QuestionAdapter getLastQuestion() {
        return last;
    }

    public void evaluateLastQuestion(int i) throws UnsupportedOperationException {
        QuestionAdapter last = this.getLastQuestion();
        if (last == null)
            throw new UnsupportedOperationException("There is no last question.");
        if (!(1 <= i && i <= last.numAnswers()))
            throw new UnsupportedOperationException("Not correct index");
        if (last.evaluate(i))
            grade++;
    }

    public int finish() {
        finished = true;
        return this.grade;
    }

    public boolean hasFinished() {
        return this.finished;
    }
}
