package com.xmas.service.questions.data;

public class FileQuestionData implements QuestionData{

    private String filePath;

    @Override
    public DataType getType() {
        return DataType.FILE;
    }

    @Override
    public void evaluateDailyData() {

    }
}
