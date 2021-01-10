package common.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

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
