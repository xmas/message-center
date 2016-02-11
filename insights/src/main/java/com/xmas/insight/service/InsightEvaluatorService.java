package com.xmas.insight.service;

import com.xmas.exceptions.NotFoundException;
import com.xmas.insight.dao.InsightEvaluatorRepository;
import com.xmas.insight.entity.InsightEvaluator;
import com.xmas.insight.validator.questionid.QuestionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class InsightEvaluatorService {

    @Value("${questions.api.url}")
    private String questionsApiUrl;

    @Autowired
    private InsightEvaluatorRepository evaluatorRepository;

    public Iterable<InsightEvaluator> getInsights() {
        return evaluatorRepository.findAll();
    }

    public InsightEvaluator getEvaluator(Long id) {
        return evaluatorRepository.findOne(id);
    }

    public void addInsightEvaluator(InsightEvaluator evaluator, MultipartFile script) {
        validateQuestion(evaluator.getQuestionId());
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
}
