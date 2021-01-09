package common.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StudentAPI implements Cloneable, JsonSerializable {

    @JsonProperty("studentID") private String studentID;

    public StudentAPI(@JsonProperty("studentID") String studentID) {
        this.studentID = studentID;
    }

    @Override
    protected Object clone() {
        return new StudentAPI(studentID);
    }

    @Override
    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    @Override
    public String toString() {
        return "StudentAPI{" +
                "studentID='" + studentID + '\'' +
                '}';
    }
}
