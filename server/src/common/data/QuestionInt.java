package common.data;

import java.util.List;

public interface QuestionInt {
    int numAnswers();

    String getQuestionTitle();

    List<Choice> getChoices();
}
