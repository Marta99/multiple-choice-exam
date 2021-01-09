package common.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class ChoiceAPI {

    @JsonProperty("choice_id") private int choice_id;
    @JsonProperty("response") private String response;

    public ChoiceAPI(@JsonProperty("choice_id") int choice_id,
                     @JsonProperty("response") String response) {
        this.choice_id = choice_id;
        this.response = response;
    }

    @Override
    public String toString() {
        return "ChoiceAPI{" +
                "choice_id=" + choice_id +
                ", response='" + response + '\'' +
                '}';
    }

}
