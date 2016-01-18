package com.xmas.config;


import com.xmas.dao.questions.QuestionRepository;
import com.xmas.service.questions.scheduller.JobDetailsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.stereotype.Component;

@Component
public class ContextStartedEventListener implements ApplicationListener<ContextStartedEvent> {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private QuestionRepository questionRepository;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private JobDetailsFactory jobDetailsFactory;

    @Override
    public void onApplicationEvent(ContextStartedEvent event) {
        standUpScheduler();
    }

    private void standUpScheduler(){
        questionRepository.getScheduled().forEach(jobDetailsFactory::addQuestionJob);
    }
}