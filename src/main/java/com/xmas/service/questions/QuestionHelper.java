package com.xmas.service.questions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.entity.questions.*;
import com.xmas.exceptions.ProcessingException;
import com.xmas.service.questions.script.ScriptEvaluator;
import com.xmas.util.FileUtil;

import java.io.IOException;
import java.nio.file.Paths;

public class QuestionHelper implements Question{

    public static final String SCRIPT_DIRECTORY_NAME = "script";
    public static final String SCRIPT_FILE_NAME = "script.sc";

    public static final String ANSWER_FILE_NAME = "answer.json";

    private static final ObjectMapper maper = new ObjectMapper();

    private com.xmas.entity.questions.Question question;

    public QuestionHelper(com.xmas.entity.questions.Question question) {
        this.question = question;
    }

    public String getScript(){
        String scriptFilePath = Paths.get(question.getDirectoryPath(), SCRIPT_DIRECTORY_NAME, SCRIPT_FILE_NAME).toString();
        return getFileAsString(scriptFilePath);
    }

    public Answer getAnsver(){
        try {
            String answerFilePath = Paths.get(question.getDirectoryPath(), ANSWER_FILE_NAME).toString();

            String rawAnswerData = getFileAsString(answerFilePath);

            return maper.reader().forType(Answer.class).readValue(rawAnswerData);
        } catch (IOException e) {
            throw new ProcessingException("Can't get answer from output file. Probably script evaluate wrong file structure");
        }
    }



    private String getFileAsString(String filePath){
        return new String(FileUtil.getFile(filePath));
    }

    @Override
    public void evaluate() {

    }

    private ScriptEvaluator getScriptEvaluator(){
        switch (question.getScriptType()){
            case R : return new
        }
    }
}
