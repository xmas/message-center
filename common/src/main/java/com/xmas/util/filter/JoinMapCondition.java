package com.xmas.util.filter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JoinMapCondition<K, V> {

    private Comparison comparison;

    private V value;

    private K key;

    private String field;
}
