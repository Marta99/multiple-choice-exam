package common.data;

import java.io.Serializable;
import java.util.Objects;

public class Choice implements Serializable {
    private final int id;
    private final String answer;


    public Choice(int id, String answer) {
        this.id = id;
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Choice choice = (Choice) o;
        return id == choice.id && answer.equals(choice.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer);
    }

    @Override
    public String toString() {
        return "Choice " + id + ": " + answer;
    }
}
