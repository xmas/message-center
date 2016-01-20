package com.xmas.entity.questions;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDate;

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
    private LocalDate date;
    @Column
    private Long guid;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getGuid() {
        return guid;
    }

    public void setGuid(Long guid) {
        this.guid = guid;
    }
}
