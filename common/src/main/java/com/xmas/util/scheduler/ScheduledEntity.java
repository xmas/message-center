package com.xmas.util.scheduler;

import com.xmas.entity.EvaluatorEntity;

/**
 * Represents entity that can be evaluated by some schedule
 */
public interface ScheduledEntity extends EvaluatorEntity{

    /**
     * @return path to directory with entity data
     */
    String getDirectoryPath();

    /**
     * @return unique identifier of this entity
     */
    Long getId();

    /**
     * Define if this entity support scheduling
     * i.e. entity can be evaluated without any user interaction
     * (Entities with file upload data source can't be scheduled)
     * @return true if this entity evaluating can be scheduled and false otherwise
     */
    boolean supportScheduling();

    /**
     * Returns schedule for evaluating this entity represented by CRON expression
     * @return CRON expression in string or null
     */
    String getCron();
}
