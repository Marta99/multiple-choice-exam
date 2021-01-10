package common.api.data;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;

public class POSTExamAPIBuilder {


    private String title, description, dateStart, dateFinish;
    private LocationAPI location;
    private final ArrayList<QuestionAPI> questions;
    private final ArrayList<StudentAPI> students;

    public POSTExamAPIBuilder() {
        title = null;
        description = null;
        dateStart = null;
        dateFinish = null;
        location = null;
        questions = new ArrayList<>();
        students = new ArrayList<>();
    }

    private POSTExamAPIBuilder(POSTExamAPIBuilder builder) {
        title = builder.title;
        description = builder.description;
        dateStart = builder.dateStart;
        dateFinish = builder.dateFinish;
        location = builder.location;
        questions = (ArrayList<QuestionAPI>) builder.questions.clone();
        students = (ArrayList<StudentAPI>) builder.students.clone();
    }

    public POSTExamAPIBuilder title(String title) {
        var newBuilder = new POSTExamAPIBuilder(this);
        newBuilder.title = title;
        return newBuilder;
    }

    public POSTExamAPIBuilder description(String description) {
        var newBuilder = new POSTExamAPIBuilder(this);
        newBuilder.description = description;
        return newBuilder;
    }

    public POSTExamAPIBuilder dateStart(String dateStart) {
        var newBuilder = new POSTExamAPIBuilder(this);
        newBuilder.dateStart = dateStart;
        return newBuilder;
    }

    public POSTExamAPIBuilder dateFinish(String dateFinish) {
        var newBuilder = new POSTExamAPIBuilder(this);
        newBuilder.dateFinish = dateFinish;
        return newBuilder;
    }

    public POSTExamAPIBuilder location(LocationAPI location) {
        var newBuilder = new POSTExamAPIBuilder(this);
        newBuilder.location = location;
        return newBuilder;
    }

    public POSTExamAPIBuilder addQuestion(QuestionAPI q) {
        var newBuilder = new POSTExamAPIBuilder(this);
        newBuilder.questions.add(q);
        return newBuilder;
    }

    public POSTExamAPIBuilder addStudent(StudentAPI student) {
        var newBuilder = new POSTExamAPIBuilder(this);
        newBuilder.students.add(student);
        return newBuilder;
    }

    public POSTExamAPI build() throws POSTExamAPIBuilderException, JsonProcessingException {
        throwIfNull(title);
        throwIfNull(description);
        throwIfNull(dateStart);
        throwIfNull(dateFinish);
        throwIfNull(location);
        if (questions.isEmpty())
            throw new POSTExamAPIBuilderException();
        if (students.isEmpty())
            throw new POSTExamAPIBuilderException();
        return new POSTExamAPI(title, description, dateStart, dateFinish, location, questions, students);
    }

    private void throwIfNull(Object value) throws POSTExamAPIBuilderException {
        if (value == null)
            throw new POSTExamAPIBuilderException();
    }
}
