package com.xmas.service.scheduller;

import com.xmas.exceptions.QuestionNotFoundException;
import com.xmas.service.EntityEvaluator;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EntityJobFactory<T> implements JobFactory {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    @Qualifier("evaluatedEntityRepository")
    private CrudRepository<T, Long> evaluatedEntityRepository;

    @Autowired
    @Qualifier("entityEvaluator")
    private EntityEvaluator<T> entityEvaluator;

    @Override
    public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {
        return new TaskExecutor<>(entityEvaluator, getQuestion(bundle));
    }

    private T getQuestion(TriggerFiredBundle bundle){
        JobDetail jobDetail = bundle.getJobDetail();
        Long qId = jobDetail.getJobDataMap().getLongValue(JobDetailsFactory.QUESTION_ID_PARAM_NAME);
        return Optional.ofNullable(evaluatedEntityRepository.findOne(qId)).orElseThrow(QuestionNotFoundException::new);
    }
}
