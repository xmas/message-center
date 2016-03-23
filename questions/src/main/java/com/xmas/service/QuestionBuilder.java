package com.xmas.service;

import com.xmas.entity.Question;
import com.xmas.entity.Tag;
import com.xmas.service.datasource.DataSourceType;
import com.xmas.util.data.DataType;
import com.xmas.util.script.ScriptType;

import java.util.Collection;
import java.util.Map;

import static com.xmas.util.json.MapParser.parseScriptArgs;

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

}
