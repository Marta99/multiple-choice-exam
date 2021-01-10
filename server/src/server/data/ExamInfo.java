package server.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import common.api.data.LocationAPI;
import common.api.data.StudentAPI;

import java.util.List;

public class ExamInfo {
    private final int id;
    private String title;
    private String description;
    private String dateStart;
    private String dateFinish;
    private LocationAPI location;
    private List<StudentAPI> students;

    public ExamInfo(@JsonProperty("id") int id,
                   @JsonProperty("title") String title, @JsonProperty("description") String description,
                   @JsonProperty("date_start") String dateStart, @JsonProperty("date_finish") String dateFinish,
                   @JsonProperty("location") LocationAPI location,
                   @JsonProperty("students") List<StudentAPI> students) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dateStart = dateStart;
        this.dateFinish = dateFinish;
        this.location = location;
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
                ", students=" + students +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDateStart() {
        return dateStart;
    }

    public String getDateFinish() {
        return dateFinish;
    }

    public LocationAPI getLocation() {
        return location;
    }

    public List<StudentAPI> getStudents() {
        return students;
    }
}
