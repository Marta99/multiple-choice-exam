package common.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GradeAPI implements JsonSerializable{
    private StudentAPI student;
    @JsonProperty("correct") private int correct;

    public GradeAPI(@JsonProperty("correct") int correct, @JsonProperty("student") StudentAPI student) {
        this.correct = correct;
        this.student = student;
    }

    @Override
    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    @Override
    public String toString() {
        return "GradeAPI{" +
                "student=" + student +
                ", correct=" + correct +
                '}';
    }
}
