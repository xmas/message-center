package com.xmas.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xmas.util.json.LocalDateTimeSerializer;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuppressWarnings("unused")
@Entity
@Table(name="answers")
@Data
public class Answer implements EvaluatedEntity{

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

    @Override
    public void setParent(Object parent) {
        if(parent instanceof Question){
            setQuestion((Question) parent);
        } else {
            throw new IllegalArgumentException("Parent for answer should be instance of Question class.");
        }
    }
}
