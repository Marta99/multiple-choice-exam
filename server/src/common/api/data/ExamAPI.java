package common.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ExamAPI {
    private final int id;
    private String title;
    private String description;
    private String dateStart;
    private String dateFinish;
    private LocationAPI location;
    private List<QuestionAPI> questions;
    private List<StudentAPI> students;

    public ExamAPI(@JsonProperty("id") int id,
                   @JsonProperty("title") String title, @JsonProperty("description") String description,
                   @JsonProperty("date_start") String dateStart, @JsonProperty("date_finish") String dateFinish,
                   @JsonProperty("location") LocationAPI location,
                   @JsonProperty("questions") List<QuestionAPI> questions,
                   @JsonProperty("students") List<StudentAPI> students) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.location = location;
        this.questions = questions;
        this.students = students;
    }

    @Override
    public String toString() {
        return "ExamAPI{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateStart='" + dateStart + '\'' +
                ", dateFinish='" + dateFinish + '\'' +
                ", location=" + location +
                ", questions=" + questions +
                ", students=" + students +
                '}';
    }
}
