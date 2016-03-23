package com.xmas.util.scheduler;

import com.xmas.exceptions.ProcessingException;
import com.xmas.util.ScheduledCrudRepository;
import org.quartz.*;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;


@Service
public class JobDetailsFactory<T extends ScheduledEntity> {

    public static final String DEFAULT_JOB_GROUP = "ENTITY";
    public static final String ENTITY_ID_PARAM_NAME = "ID";

    private Scheduler scheduler;

    @Autowired
    private JobFactory jobFactory;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    @Qualifier("evaluatedEntityRepository")
    private ScheduledCrudRepository<T> evaluatedEntityRepository;

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

    public void addQuestionJob(T entity) {
        try {
            scheduler.scheduleJob(buildJob(entity), buildTrigger(entity));
        } catch (SchedulerException e) {
            throw new ProcessingException("Cant schedule evaluating entity. " +  e.getMessage(), e);
        }
    }

    private JobDetail buildJob(T entity){
        return newJob()
                .withIdentity(entity.getDirectoryPath(), DEFAULT_JOB_GROUP)
                .usingJobData(ENTITY_ID_PARAM_NAME, entity.getId())
                .ofType(TaskExecutor.class)
                .build();
    }

    private Trigger buildTrigger(T entity){
        return newTrigger()
                .withSchedule(cronSchedule(entity.getCron()))
                .build();
    }

    private void standUpScheduler(){
        evaluatedEntityRepository.getScheduled().stream()
                .filter(ScheduledEntity::supportScheduling)
                .forEach(this::addQuestionJob);
    }

}
