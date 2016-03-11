package com.xmas.service.scheduller;

import com.xmas.entity.Question;
import com.xmas.service.EntityEvaluator;
import com.xmas.service.QuestionHelper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TaskExecutor<T> implements Job{

    private EntityEvaluator entityEvaluator;

    private Question question;

    public TaskExecutor(QuestionHelper questionHelper, Question question) {
        this.entityEvaluator = questionHelper;
        this.question = question;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        entityEvaluator.evaluate(question);
    }

}
