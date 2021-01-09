package common.api.data;

import common.data.Choice;
import common.data.Question;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExamAPITest {

    private String title = "Distributed Computing Unit 1";
    private String description = "Exam of the first unit of Distributed Computing subject";
    private String dateStart = "2021-01-04T01:36:00Z";
    private String dateFinish = "2021-01-05T01:36:00Z";
    private LocationAPI location = new LocationAPI(998, "localhost", "string1");
    private List<Question> questions = List.of(new Question("What is a Middleware?", List.of(new Choice(1, "Nothing"))));
    private ArrayList<StudentAPI> students;

}