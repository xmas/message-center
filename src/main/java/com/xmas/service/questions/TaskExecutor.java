package com.xmas.service.questions;

import com.xmas.entity.questions.Question;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Collection;

public class TaskExecutor implements Job{

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

    }

    private Collection<Question> getQuestionsToExecute(JobExecutionContext jobExecutionContext){
        jobExecutionContext.getTrigger().getDescription();
        return null;
    }

}
