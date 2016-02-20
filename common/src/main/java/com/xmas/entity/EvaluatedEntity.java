package com.xmas.entity;

import java.time.LocalDateTime;

/**
 * Common interface for classes instances of which  is created by script
 */
public interface EvaluatedEntity {

    /**
     * Set parent objesct for this entity
     * i.e. Question for Answer
     * @param parent instance for parent entity
     */
    void setParent(Object parent);

    /**
     * Set DateTime when this object was created
     * @param date Date of creation
     */
    void setDate(LocalDateTime date);
}
