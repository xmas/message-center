package com.xmas.entity.questions;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xmas.util.json.LocalDateTimeSerializer;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Entity
@Table(name="answers")
public class Answer {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String title;
    @Column
    private String details;
    @Column
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime date;
    @Column
    private String path;
    @Column
    private Long guid;
    @Transient
    private String dataDir;
    @ManyToOne
    @JoinColumn(name = "question")
    @JsonIgnore
    private Question question;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Long getGuid() {
        return guid;
    }

    public void setGuid(Long guid) {
        this.guid = guid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDataDir() {
        return question.getDirectoryPath();
    }

    public void setDataDir(String dataDir) {
        this.dataDir = dataDir;
    }
}
