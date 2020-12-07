package common.data;

import java.io.Serializable;
import java.util.Objects;

public class Choice implements Serializable {
    private final int id;
    private final String question;


    public Choice(int id, String question) {
        this.id = id;
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Choice choice = (Choice) o;
        return id == choice.id && question.equals(choice.question);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, question);
    }

    @Override
    public String toString() {
        return "Choice{" +
                "id=" + id +
                ", question='" + question + '\'' +
                '}';
    }
}
