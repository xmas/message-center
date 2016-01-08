package com.xmas.service.questions.data;

import com.xmas.service.questions.datasource.DataSource;

public interface QuestionData{

    DataType getType();

    void evaluateDailyData();

    void setDataSource(DataSource dataSource);

}
