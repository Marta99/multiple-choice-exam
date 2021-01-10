package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import common.api.data.POSTExamAPI;
import common.api.data.QuestionAPI;
import org.junit.jupiter.api.Test;
import server.data.ExamInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class QuestionConverse {

    @Test
    public void test() throws IOException {
        var pathInfoExam = "./data/exam.json";
        List<QuestionAPI> questions = new CSVReader(new BufferedReader(new FileReader("./data/exam1.csv")))
                .getQuestions()
                .stream().map(QuestionAdapter::toQuestionAPI).collect(Collectors.toList());
        ExamInfo exam = new ObjectMapper().readValue(new File(pathInfoExam), ExamInfo.class);
        System.out.println(POSTExamAPI.fromExamInfo(exam, questions).toJson());
    }
}
