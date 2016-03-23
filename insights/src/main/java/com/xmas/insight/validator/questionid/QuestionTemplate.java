package com.xmas.insight.validator.questionid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class QuestionTemplate {
    private Integer id;
    private String directoryPath;
}
