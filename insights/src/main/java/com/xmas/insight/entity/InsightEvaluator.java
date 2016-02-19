package com.xmas.insight.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xmas.util.json.LocalDateTimeSerializer;
import com.xmas.util.script.ScriptType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Map;

import static com.xmas.util.ValidationUtil.CRON_REGEX;

@Entity
@Table(name = "insight_data")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsightEvaluator {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Long questionId;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="script_args")
    @MapKeyColumn (name="name")
    @Column(name="value")
    private Map<String, String> scriptArgs;
}
