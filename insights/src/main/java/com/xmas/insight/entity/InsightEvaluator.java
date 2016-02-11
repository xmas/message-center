package com.xmas.insight.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xmas.util.json.LocalDateTimeSerializer;
import com.xmas.util.script.ScriptType;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

import static com.xmas.util.ValidationUtil.CRON_REGEX;

@Entity
@Table(name = "insight_data")
@Data
public class InsightEvaluator {
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

    @Column
    private String directoryPath;

    @Column
    @Pattern(regexp = CRON_REGEX, message = "Not valid CRON expression.")
    private String cron;
}
