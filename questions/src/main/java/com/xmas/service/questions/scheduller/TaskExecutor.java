package com.xmas.service.questions.scheduller;

import com.xmas.entity.questions.Question;
import com.xmas.service.questions.QuestionEvaluator;
import com.xmas.service.questions.QuestionHelper;
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
