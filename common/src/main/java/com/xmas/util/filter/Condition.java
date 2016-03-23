package com.xmas.util.filter;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Condition {

    private Comparison comparison;

    private Object value;

    private String field;

}
