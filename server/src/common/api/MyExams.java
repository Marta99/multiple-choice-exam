package common.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.api.data.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Optional;

public class MyExams {


    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String url = "http://localhost:8000/";
    private static final String getlistExamEndpoint = url + "api/exam/";
    private static final String postExamEndpoint = url + "api/exam/";

    private static URI getAccessURI(int examID, String studentID) {
        return URI.create(url + "api/exam/" + examID + "/" + studentID + "/");
    }

    private static URI getQuestionURI(int questionUniqueID) {
        return URI.create(url + "api/question/" + questionUniqueID + "/");
    }

    private static URI getExamURI(int examID) {
        return URI.create(url + "api/exam/" + examID + "/");
    }

    private static final URI getListExamURI = URI.create(url + "api/exam/");

    public static Optional<ExamAPI> getExam(int examID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(getExamURI(examID))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200)
            return Optional.empty();
        return Optional.of(new ObjectMapper()
                .readValue(response.body(), ExamAPI.class));
    }

    public static Optional<ExamAPI> postExam(POSTExamAPI exam) throws IOException, InterruptedException {
        var json = exam.toJson();
        System.out.println(json);
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .uri(getListExamURI)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 201)
            return Optional.empty();
        return Optional.of(new ObjectMapper()
                .readValue(response.body(), ExamAPI.class));
    }


    public static void storeGrades() {

    }

    public static Optional<LocationAPI> verifyStudentID(int examID, String studentID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(getAccessURI(examID, studentID))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200)
            return Optional.empty();
        return Optional.of(new ObjectMapper()
                .readValue(response.body(), LocationAPI.class));
    }

    public static Optional<QuestionAPI> getQuestion(int questionUniqueID) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("accept", "application/json")
                .uri(getQuestionURI(questionUniqueID))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200)
            return Optional.empty();
        return Optional.of(new ObjectMapper()
                .readValue(response.body(), QuestionAPI.class));
    }

    public static void main(String[] args) throws IOException, InterruptedException, POSTExamAPIBuilderException {
        Optional<LocationAPI> locationAPI = verifyStudentID(1, "423");
        locationAPI.ifPresent(System.out::println);
        getExam(1).ifPresent(System.out::println);
        getQuestion(1).ifPresent(System.out::println);
        var exam = new POSTExamAPIBuilder()
                .title("My title")
                .description("My description")
                .dateStart("2021-01-04T01:36:00Z")
                .dateFinish("2021-01-05T11:40:00Z")
                .location(new LocationAPI(1, "host", "bind"))
                .addQuestion(new QuestionAPI("Title", List.of(new ChoiceAPI(1, "Reponse")), 1))
                .addStudent(new StudentAPI("STUDENTID1"))
                .build();
        postExam(exam).ifPresent(System.out::println);
    }


}
