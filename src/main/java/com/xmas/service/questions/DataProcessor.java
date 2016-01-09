package com.xmas.service.questions;

@FunctionalInterface
public interface DataProcessor<T, V> {
    T processData(V data);
}
