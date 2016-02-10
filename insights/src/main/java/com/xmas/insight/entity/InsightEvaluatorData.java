package com.xmas.insight.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xmas.util.json.LocalDateTimeSerializer;
import com.xmas.util.script.ScriptType;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "insight_data")
@Data
public class InsightEvaluatorData {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long questionId;

    @Column
    private String scriptFile;

    @Column
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime lastTimeEvaluated;

    @Enumerated(EnumType.STRING)
    private ScriptType scriptType;
}
