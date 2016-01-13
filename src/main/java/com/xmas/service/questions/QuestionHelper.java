package com.xmas.service.questions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.entity.questions.Answer;
import com.xmas.entity.questions.Question;
import com.xmas.exceptions.ProcessingException;
import com.xmas.service.questions.script.ScriptFileUtil;
import com.xmas.service.questions.script.ScriptService;
import com.xmas.util.FileUtil;
import com.xmas.util.RandomNamesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class QuestionHelper {

    public static final String ANSWER_FILE_NAME = "answer.json";

    public static final String QUESTIONS_BASE_DIR_PATH = QuestionHelper.class.getResource("/questions").getPath();

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ScriptService scriptService;

    public void saveQuestion(Question question) {

    }

    public Answer getAnsver(Question question) {
        try {
            String answerFilePath = Paths.get(question.getDirectoryPath(), ANSWER_FILE_NAME).toString();

            String rawAnswerData = new String(FileUtil.getFile(answerFilePath));

            return mapper.reader().forType(Answer.class).readValue(rawAnswerData);
        } catch (IOException e) {
            throw new ProcessingException("Can't get answer from output file. Probably script evaluate wrong file structure");
        }
    }

    public void evaluate(Question question) {
        scriptService.evaluate(question.getScriptType(),
                ScriptFileUtil.getScript(question.getDirectoryPath()),
                question.getDirectoryPath());
    }

    private File createQuestionDirectory() {
        String questionDirPath = Paths.get(QUESTIONS_BASE_DIR_PATH, RandomNamesUtil.getRandomName())
                .toAbsolutePath()
                .toString();

        return FileUtil.createDirectory(questionDirPath);
    }

}
