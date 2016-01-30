package com.xmas.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.entity.Question;
import com.xmas.entity.Tag;
import com.xmas.exceptions.BadRequestException;
import com.xmas.service.data.DataType;
import com.xmas.service.datasource.DataSourceType;
import com.xmas.service.script.ScriptType;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class QuestionBuilder {

    private Collection<Tag> tags;

    private String dataSourceResource;

    private DataSourceType dataSourceType;

    private DataType dataType;

    private ScriptType scriptType;

    private String cron;

    private Map<String, String> scriptArgs;

    private QuestionBuilder() {

    }

    public static QuestionBuilder createQuestion() {
        return new QuestionBuilder();
    }

    public QuestionBuilder withTags(Collection<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public QuestionBuilder withDataSourceResource(String dataSourceResource) {
        this.dataSourceResource = dataSourceResource;
        return this;
    }

    public QuestionBuilder withDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
        return this;
    }

    public QuestionBuilder withDataType(DataType dataType) {
        this.dataType = dataType;
        return this;
    }

    public QuestionBuilder withScriptType(ScriptType scriptType){
        this.scriptType = scriptType;
        return this;
    }

    public QuestionBuilder withCron(String cron){
        this.cron = cron;
        return this;
    }

    public QuestionBuilder withScriptArgs(String scriptArgs){
        this.scriptArgs = parseScriptArgs(scriptArgs);
        return this;
    }

    public Question build(){
        Question question = new Question();
        question.setTags(tags);
        question.setDataSourceResource(dataSourceResource);
        question.setDataSourceType(dataSourceType);
        question.setDataType(dataType);
        question.setScriptType(scriptType);
        question.setCron(cron);
        question.setScriptArgs(scriptArgs);
        return question;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> parseScriptArgs(String string){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(string, Map.class);
        } catch (IOException e) {
            throw new BadRequestException("Can't parse string \"" + string + "\" as script arguments.", e);
        }
    }
}
