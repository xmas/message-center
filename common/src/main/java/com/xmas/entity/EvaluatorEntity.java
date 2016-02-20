package com.xmas.entity;

import java.time.LocalDateTime;

/**
 * Common interface for classes that represents evaluation entity
 * Like question or InsightEvaluator
 */
public interface EvaluatorEntity {

    /**
     * @return dateTime when this entity was evaluated last time
     */
    LocalDateTime getLastTimeEvaluated();
}
