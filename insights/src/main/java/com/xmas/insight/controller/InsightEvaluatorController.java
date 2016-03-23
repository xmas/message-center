package com.xmas.insight.controller;

import com.xmas.insight.entity.Insight;
import com.xmas.insight.entity.InsightEvaluator;
import com.xmas.insight.service.InsightEvaluatorService;
import com.xmas.util.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.xmas.util.json.MapParser.parseScriptArgs;

@RestController
@RequestMapping("/evals")
public class InsightEvaluatorController {

    @Autowired
    private InsightEvaluatorService insightEvaluatorService;

    @RequestMapping
    public Iterable<InsightEvaluator> getInsightEvaluators(@RequestParam Long questionId) {
        return insightEvaluatorService.getInsights(questionId);
    }

    @RequestMapping("/{insightId}")
    public InsightEvaluator getInsightEvaluator(@RequestParam Long questionId,
                                                @PathVariable Long insightId) {
        return insightEvaluatorService.getEvaluator(questionId, insightId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addInsightEvaluator(@RequestParam Long questionId,
                                    @RequestParam MultipartFile script,
                                    @RequestParam(required = false) String scriptArgs,
                                    @RequestParam(required = false) String cron,
                                    @RequestParam ScriptType scriptType) {
        InsightEvaluator evaluator = InsightEvaluator.builder()
                .scriptArgs(scriptArgs == null ? null : parseScriptArgs(scriptArgs))
                .cron(cron)
                .questionId(questionId)
                .scriptType(scriptType)
                .build();

        insightEvaluatorService.addInsightEvaluator(evaluator, script);
    }

    @RequestMapping(value = "/{insightId}", method = RequestMethod.POST)
    public List<Insight> evaluateInsight(@PathVariable Long insightId){
        return insightEvaluatorService.evaluate(insightId);
    }

}
