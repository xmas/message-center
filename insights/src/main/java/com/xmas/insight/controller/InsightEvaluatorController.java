package com.xmas.insight.controller;

import com.xmas.insight.entity.InsightEvaluator;
import com.xmas.insight.service.InsightEvaluatorService;
import com.xmas.util.script.ScriptType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.xmas.util.json.MapParser.parseScriptArgs;

@RestController
@RequestMapping("/questions/{questionId}/insightEval")
public class InsightEvaluatorController {

    @Autowired
    private InsightEvaluatorService insightEvaluatorService;

    @RequestMapping
    public Iterable<InsightEvaluator> getInsightEvaluators(@PathVariable Long questionId) {
        return insightEvaluatorService.getInsights(questionId);
    }

    @RequestMapping("/{insightId}")
    public InsightEvaluator getInsightEvaluator(@PathVariable Long questionId,
                                                @PathVariable Long insightId) {
        return insightEvaluatorService.getEvaluator(questionId, insightId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addInsightEvaluator(@PathVariable Long questionId,
                                    @RequestParam MultipartFile script,
                                    @RequestParam(required = false) String scriptArgs,
                                    @RequestParam(required = false) String cron,
                                    @RequestParam ScriptType scriptType) {
        InsightEvaluator evaluator = InsightEvaluator.builder()
                .scriptArgs(parseScriptArgs(scriptArgs))
                .cron(cron)
                .questionId(questionId)
                .scriptType(scriptType)
                .build();

        insightEvaluatorService.addInsightEvaluator(evaluator, script);
    }

    @RequestMapping(value = "/{insightId}", method = RequestMethod.POST)
    public void evaluateInsight(){

    }

}
