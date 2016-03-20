package com.xmas.insight.service;

import com.xmas.exceptions.BadRequestException;
import com.xmas.exceptions.NotFoundException;
import com.xmas.insight.dao.InsightEvaluatorRepository;
import com.xmas.insight.dao.InsightRepository;
import com.xmas.insight.entity.Insight;
import com.xmas.insight.entity.InsightEvaluator;
import com.xmas.insight.validator.questionid.QuestionTemplate;
import com.xmas.util.scheduller.JobDetailsFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;

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

    private RestTemplate restTemplate;

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
        return insightRepository.find().stream()
                .filter(i -> i.getEvaluator().equals(evaluator))
                .filter(i -> i.getDate().equals(evaluator.getLastTimeEvaluated()))
                .collect(Collectors.toList()
        );

    }

    public void validateQuestion(Long id) {
        try {
            QuestionTemplate question = restTemplate.getForEntity(questionsApiUrl + "/" + id, QuestionTemplate.class).getBody();
            if (question == null)
                throw new NotFoundException("There is no question with such id or Question service is unreachable.");
        } catch (RestClientException rce) {
            throw new NotFoundException("There is no question with such id or Question service is unreachable.", rce);
        }
    }

    @PostConstruct
    private void prepareTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();
        HttpComponentsClientHttpRequestFactory requestFactory =
        new HttpComponentsClientHttpRequestFactory();

        requestFactory.setHttpClient(httpClient);

        restTemplate = new RestTemplate(requestFactory);
    }

    private void scheduleQuestionEvaluating(InsightEvaluator insightEvaluator) {
        if (!insightEvaluator.supportScheduling()) {
            throw new BadRequestException("Question with such type can't be scheduled for evaluating.");
        } else if (insightEvaluator.getCron() != null) {
            jobDetailsFactory.addQuestionJob(insightEvaluator);
        }
    }
}
