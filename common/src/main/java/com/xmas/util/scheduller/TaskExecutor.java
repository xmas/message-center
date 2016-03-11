package com.xmas.util.scheduller;

import com.xmas.util.EntityEvaluator;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TaskExecutor<T> implements Job{

    private EntityEvaluator<T> entityEvaluator;

    private T entity;

    public TaskExecutor(EntityEvaluator<T> entityEvaluator, T entity) {
        this.entityEvaluator = entityEvaluator;
        this.entity = entity;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        entityEvaluator.evaluate(entity);
    }

}
