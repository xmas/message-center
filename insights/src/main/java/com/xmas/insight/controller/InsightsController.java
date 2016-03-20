package com.xmas.insight.controller;

import com.xmas.insight.dao.InsightRepository;
import com.xmas.insight.entity.Insight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/")
public class InsightsController {

    @Autowired
    private InsightRepository insightRepository;

    @RequestMapping
    public Iterable<Insight> getInsights(@RequestParam(required = false) Long question,
                                         @RequestParam(required = false) Long eval,
                                         @RequestParam(required = false) LocalDate from,
                                         @RequestParam(required = false) LocalDate to) {
        return insightRepository
                .find().stream()
                .filter(i -> question == null || i.getEvaluator().getQuestionId().equals(question))
                .filter(i -> eval == null || i.getEvaluator().getId().equals(eval))
                .filter(i -> from == null || i.getDate().isAfter(from.atStartOfDay()))
                .filter(i -> to == null || i.getDate().isBefore(to.atStartOfDay()))
                .collect(Collectors.toList());

    }

}
