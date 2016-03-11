package com.xmas.insight.service;

import com.xmas.exceptions.BadRequestException;
import com.xmas.exceptions.NotFoundException;
import com.xmas.insight.dao.InsightEvaluatorRepository;
import com.xmas.insight.dao.InsightRepository;
import com.xmas.insight.entity.Insight;
import com.xmas.insight.entity.InsightEvaluator;
import com.xmas.insight.validator.questionid.QuestionTemplate;
import com.xmas.util.scheduller.JobDetailsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class InsightEvaluatorService {

    @Value("${questions.api.url}")
    private String questionsApiUrl;

    @Autowired
    private InsightEvaluatorHelper helper;

    @Autowired
    private InsightEvaluatorRepository evaluatorRepository;

    @Autowired
    private InsightRepository insightRepository;

    @Autowired
    private JobDetailsFactory<InsightEvaluator> jobDetailsFactory;

    public Iterable<InsightEvaluator> getInsights(Long questionId) {
        validateQuestion(questionId);
        return evaluatorRepository.getByQuestionId(questionId);
    }

    public InsightEvaluator getEvaluator(Long questionId, Long id) {
        validateQuestion(questionId);
        InsightEvaluator evaluator = evaluatorRepository.findOne(id);
        if(evaluator.getQuestionId().equals(questionId)){
            return evaluator;
        }else {
            throw new NotFoundException("This question don't has insight with such id.");
        }
    }

    public void addInsightEvaluator(InsightEvaluator evaluator, MultipartFile script) {
        validateQuestion(evaluator.getQuestionId());
        helper.saveInsightEvaluator(evaluator, script);
        if (evaluator.getCron() != null && evaluator.supportScheduling())
            scheduleQuestionEvaluating(evaluator);
    }

    public List<Insight> evaluate(Long evaluatorId){
        InsightEvaluator evaluator = evaluatorRepository.findOne(evaluatorId);
        if(evaluator == null) throw new NotFoundException("Evaluator with id " + evaluatorId + " not found.");
        helper.evaluate(evaluator);
        return insightRepository.find(evaluator, evaluator.getLastTimeEvaluated());

    }

    public void validateQuestion(Long id) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            QuestionTemplate question = restTemplate.getForEntity(questionsApiUrl + "/" + id, QuestionTemplate.class).getBody();
            if (question == null)
                throw new NotFoundException("There is no question with such id or Question service is unreachable.");
        } catch (RestClientException rce) {
            throw new NotFoundException("There is no question with such id or Question service is unreachable.", rce);
        }
    }

    private void scheduleQuestionEvaluating(InsightEvaluator insightEvaluator) {
        if (!insightEvaluator.supportScheduling()) {
            throw new BadRequestException("Question with such type can't be scheduled for evaluating.");
        } else if (insightEvaluator.getCron() != null) {
            jobDetailsFactory.addQuestionJob(insightEvaluator);
        }
    }
}
