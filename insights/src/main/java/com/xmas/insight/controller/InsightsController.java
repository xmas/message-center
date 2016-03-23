package com.xmas.insight.controller;

import com.xmas.insight.entity.Insight;
import com.xmas.insight.service.InsightsFilterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;

@RestController
@RequestMapping("/")
public class InsightsController {

    @Autowired
    private InsightsFilterService filterService;


    @RequestMapping
    public Iterable<Insight> getInsights(ServletRequest request) {
        return filterService.getInsights(request);

    }


}
