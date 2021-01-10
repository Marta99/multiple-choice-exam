package common.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import server.data.ExamInfo;

import java.util.ArrayList;
import java.util.List;

public class POSTExamAPI implements JsonSerializable {
    @JsonProperty("title")
    private final String title;
    @JsonProperty("description")
    private final String description;
    @JsonProperty("date_start")
    private final String dateStart;
    @JsonProperty("date_finish")
    private final String dateFinish;
    @JsonProperty("location")
    private final LocationAPI location;
    @JsonProperty("questions")
    private final ArrayList<QuestionAPI> questions;
    @JsonProperty("students")
    private final ArrayList<StudentAPI> students;

    public POSTExamAPI(@JsonProperty("title") String title, @JsonProperty("description") String description,
                       @JsonProperty("date_start") String dateStart, @JsonProperty("date_finish") String dateFinish,
                       @JsonProperty("location") LocationAPI location,
                       @JsonProperty("questions") List<QuestionAPI> questions,
                       @JsonProperty("students") List<StudentAPI> students) {
        this.title = title;
        this.description = description;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.location = location;
        this.questions = new ArrayList<>(questions);
        this.students = new ArrayList<>(students);
    }

    @Override
    public String toJson() throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(this);
    }

    public static POSTExamAPI fromExamInfo(ExamInfo exam, List<QuestionAPI> questions) {
        return new POSTExamAPI(exam.getTitle(), exam.getDescription(), exam.getDateStart(), exam.getDateFinish(), exam.getLocation(), questions, exam.getStudents());
    }
}
