package common.data;

import java.io.Serializable;

public class Choice implements Serializable {
    private final int id;
    private final String question;


    public Choice(int id, String question) {
        this.id = id;
        this.question = question;
    }
}
