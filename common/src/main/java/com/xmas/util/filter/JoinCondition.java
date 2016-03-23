package com.xmas.util.filter;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JoinCondition {

    private Comparison comparison;

    private Class joinClass;

    private String joinAttributeName;

    private Object value;

    private String field;

}
