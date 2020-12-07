package common.data;

import common.MultipleChoiceClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Exam implements Iterator<Question> {
    private final Iterator<Question> iterator;
    private final MultipleChoiceClient student;
    private boolean finished;

    public Exam (MultipleChoiceClient c, List<Question> questions) {
        this.student = c;
        ArrayList<Question> questions1 = new ArrayList<Question>(questions);
        finished = false;
        this.iterator = questions1.iterator();
    }

    public void finish(){
        finished = true;
    }

    public MultipleChoiceClient getStudent() {
        return student;
    }

    @Override
    public boolean hasNext() {
        boolean hasNext = this.iterator.hasNext();
        if (!hasNext && !finished)
            finished = true;
        return hasNext;
    }

    @Override
    public Question next() {
        return this.iterator.next();
    }
}
