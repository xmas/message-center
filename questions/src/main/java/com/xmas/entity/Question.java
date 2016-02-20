package com.xmas.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xmas.util.data.DataType;
import com.xmas.service.datasource.DataSourceType;
import com.xmas.util.json.LocalDateTimeSerializer;
import com.xmas.util.script.ScriptType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

@Entity
@Table(name = "questions")
@Data
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue
    private Long id;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="script_args")
    @MapKeyColumn (name="name")
    @Column(name="value")
    private Map<String, String> scriptArgs;

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
}
