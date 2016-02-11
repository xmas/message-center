package com.xmas.insight.controller;

import com.xmas.insight.entity.InsightEvaluator;
import com.xmas.insight.service.InsightEvaluatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/questions/{questionId}/insightEval")
public class InsightEvaluatorController {

    @Autowired
    private InsightEvaluatorService insightEvaluatorService;

    @RequestMapping
    public Iterable<InsightEvaluator> getInsightEvaluators(){
        return insightEvaluatorService.getInsights();
    }


}
