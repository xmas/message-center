package com.xmas.service.scheduller;

import com.xmas.dao.QuestionRepository;
import com.xmas.entity.Question;
import com.xmas.exceptions.QuestionNotFoundException;
import com.xmas.service.QuestionHelper;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuestionJobFactory implements JobFactory {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionHelper questionHelper;

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        return new TaskExecutor(questionHelper, getQuestion(bundle));
    }

    private Question getQuestion(TriggerFiredBundle bundle){
        JobDetail jobDetail = bundle.getJobDetail();
        Integer qId = jobDetail.getJobDataMap().getIntValue(JobDetailsFactory.QUESTION_ID_PARAM_NAME);
        return questionRepository.getById(qId).orElseThrow(QuestionNotFoundException::new);
    }
}
