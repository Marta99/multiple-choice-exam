package common.data;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.ArrayList;
import java.util.List;

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
}