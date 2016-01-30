package com.xmas.service.scheduller;

import com.xmas.entity.Question;
import com.xmas.service.QuestionEvaluator;
import com.xmas.service.QuestionHelper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TaskExecutor implements Job{

    private QuestionEvaluator questionEvaluator;

    private Question question;

    public TaskExecutor(QuestionHelper questionHelper, Question question) {
        this.questionEvaluator = questionHelper;
        this.question = question;
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        questionEvaluator.evaluate(question);
    }

}
