package common.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class QuestionAPI implements JsonSerializable {

    @JsonProperty("title")
    private String title;
    @JsonProperty("choices")
    private List<ChoiceAPI> choices;
    @JsonProperty("correct_choice")
    private int correctChoice;

    public QuestionAPI(@JsonProperty("title") String title,
                       @JsonProperty("choices") List<ChoiceAPI> choices,
                       @JsonProperty("correct_choice") int correctChoice) {

        this.title = title;
        this.choices = choices;
        this.correctChoice = correctChoice;
    }

    @Override
    public String toString() {
        return "QuestionAPI{" +
                "title='" + title + '\'' +
                ", choices=" + choices +
                ", correctChoice=" + correctChoice +
                '}';
    }

    @Override
    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

}
