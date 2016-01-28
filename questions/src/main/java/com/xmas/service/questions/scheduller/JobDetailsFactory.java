package com.xmas.service.questions.scheduller;

import com.xmas.dao.questions.QuestionRepository;
import com.xmas.entity.questions.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;


@Service
public class JobDetailsFactory {

    private static final Logger logger = LogManager.getLogger();

    public static final String DEFAULT_JOB_GROUP = "QUESTIONS";
    public static final String QUESTION_ID_PARAM_NAME = "QID";

    private Scheduler scheduler;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private JobFactory jobFactory;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private QuestionRepository questionRepository;

    @PostConstruct
    public void init() throws SchedulerException {
        SchedulerFactory schedulerFactory = new org.quartz.impl.StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
        scheduler.setJobFactory(jobFactory);
        scheduler.start();

        standUpScheduler();
    }

    @PreDestroy
    public void destroy(){
        try {
            scheduler.shutdown();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    public void addQuestionJob(Question question) {
        try {
            scheduler.scheduleJob(buildJob(question), buildTrigger(question));
        } catch (SchedulerException e) {
            logger.error("Cant schedule evaluating question. " +  e.getMessage());
            logger.debug(e.getMessage(), e);
        }
    }

    private JobDetail buildJob(Question question){
        return newJob()
                .withIdentity(question.getDirectoryPath(), DEFAULT_JOB_GROUP)
                .usingJobData(QUESTION_ID_PARAM_NAME, question.getId())
                .ofType(TaskExecutor.class)
                .build();
    }

    private Trigger buildTrigger(Question question){
        return newTrigger()
                .withSchedule(cronSchedule(question.getCron()))
                .build();
    }

    private void standUpScheduler(){
        questionRepository.getScheduled().stream()
                .filter(question -> question.getDataSourceType().supportScheduling())
                .forEach(this::addQuestionJob);
    }

}
