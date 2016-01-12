package com.xmas.entity.questions;

import com.xmas.service.questions.data.DataType;
import com.xmas.service.questions.datasource.DataSourceType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToMany
    @JoinTable(name = "messages_tags",
            joinColumns = @JoinColumn(name = "message"),
            inverseJoinColumns = @JoinColumn(name = "tag"))
    private Collection<Tag> tags;

    @Column
    private String directoryPath;

    @Column
    private LocalDateTime lastTimeEvaluated;

    @Enumerated(EnumType.STRING)
    private DataSourceType dataSourceType;

    @Enumerated(EnumType.STRING)
    private DataType dataType;

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
}
