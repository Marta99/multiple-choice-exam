package common.data;

import java.util.ArrayList;
import java.util.Iterator;

public class Exam implements Iterator<Question> {
    private final Iterator<Question> iterator;
    private final ArrayList<Question> questions;
    private boolean finished;

    public Exam (ArrayList<Question> questions) {
        this.questions = new ArrayList<Question>(questions);
        finished = false;
        this.iterator = this.questions.iterator();
    }

    public void finish(){
        finished = true;
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public Question next() {
        return this.iterator.next();
    }
}
