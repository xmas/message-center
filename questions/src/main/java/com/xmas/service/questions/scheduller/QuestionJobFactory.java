package com.xmas.service.questions.scheduller;

import com.xmas.dao.questions.QuestionRepository;
import com.xmas.entity.questions.Question;
import com.xmas.exceptions.questions.QuestionNotFoundException;
import com.xmas.service.questions.QuestionHelper;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.xmas.service.questions.scheduller.JobDetailsFactory.QUESTION_ID_PARAM_NAME;

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
        Integer qId = jobDetail.getJobDataMap().getIntValue(QUESTION_ID_PARAM_NAME);
        return questionRepository.getById(qId).orElseThrow(QuestionNotFoundException::new);
    }
}
