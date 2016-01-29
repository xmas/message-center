package com.xmas.entity.questions;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xmas.service.questions.data.DataType;
import com.xmas.service.questions.datasource.DataSourceType;
import com.xmas.service.questions.script.ScriptType;
import com.xmas.util.json.LocalDateTimeSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@Entity
@Table(name = "questions")
@SuppressWarnings("unused")
public class Question {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToMany
    @JoinTable(name = "questions_tags",
            joinColumns = @JoinColumn(name = "question"),
            inverseJoinColumns = @JoinColumn(name = "tag"))
    private Collection<Tag> tags;

    @Column
    private String directoryPath;

    @Column
    private String dataSourceResource;

    @Column
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastTimeEvaluated;

    @Enumerated(EnumType.STRING)
    private DataSourceType dataSourceType;

    @Enumerated(EnumType.STRING)
    private DataType dataType;

    @Enumerated(EnumType.STRING)
    private ScriptType scriptType;

    @Column
    private String cron;

    @ElementCollection
    @CollectionTable(name="script_args")
    @MapKeyColumn (name="name")
    @Column(name="value")
    private Map<String, String> scriptArgs;

    public Question() {
    }

    public Question(Collection<Tag> tags,
                    DataSourceType dataSourceType,
                    String dataSourceResource,
                    DataType dataType,
                    ScriptType scriptType,
                    String cron) {
        this.tags = tags;
        this.dataSourceType = dataSourceType;
        this.dataType = dataType;
        this.scriptType = scriptType;
        this.dataSourceResource = dataSourceResource;
        this.cron = cron;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Collection<Tag> getTags() {
        return tags;
    }

    public void setTags(Collection<Tag> tags) {
        this.tags = tags;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public void setDirectoryPath(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public LocalDateTime getLastTimeEvaluated() {
        return lastTimeEvaluated;
    }

    public void setLastTimeEvaluated(LocalDateTime lastTimeEvaluated) {
        this.lastTimeEvaluated = lastTimeEvaluated;
    }

    public DataSourceType getDataSourceType() {
        return dataSourceType;
    }

    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public ScriptType getScriptType() {
        return scriptType;
    }

    public void setScriptType(ScriptType scriptType) {
        this.scriptType = scriptType;
    }

    public String getDataSourceResource() {
        return dataSourceResource;
    }

    public void setDataSourceResource(String dataSourceResource) {
        this.dataSourceResource = dataSourceResource;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public Map<String, String> getScriptArgs() {
        return scriptArgs;
    }

    public void setScriptArgs(Map<String, String> scriptArgs) {
        this.scriptArgs = scriptArgs;
    }
}
