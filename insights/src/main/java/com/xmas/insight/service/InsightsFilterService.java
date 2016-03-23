package com.xmas.insight.service;

import com.xmas.exceptions.BadRequestException;
import com.xmas.insight.entity.Insight;
import com.xmas.insight.entity.InsightEvaluator;
import com.xmas.util.filter.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class InsightsFilterService {

    @Autowired
    JpaSpecificationExecutor<Insight> insightsRepository;

    private static final Set<String> PREDEFINED_REQUEST_PARAMS_NAMES = new HashSet<String>() {{
        add("question");
        add("eval");
        add("from");
        add("to");
    }};

    public Iterable<Insight> getInsights(ServletRequest request) {
        return insightsRepository.findAll(buildFilter(request));
    }

    private EntityFilter<Insight> buildFilter(ServletRequest request) {
        Map<String, String> params = getPredefinedParams(request);

        return addNonPredefinedParams(request)

                .withCondition(Condition.builder()
                        .comparison(Comparison.EQUAL)
                        .field("evaluator")
                        .value(params.get("eval") != null ? Long.valueOf(params.get("eval")) : null)
                        .build())

                .withCondition(Condition.builder()
                        .comparison(Comparison.BETWEEN)
                        .field("date")
                        .value(new HashMap<String, LocalDateTime>() {{
                            put(EntityFilter.LOWER_LIMIT_PARAM_NAME, LocalDateTime.parse(Optional.ofNullable(params.get("from")).orElseGet(LocalDateTime.MIN::toString)));
                            put(EntityFilter.UPPER_LIMIT_PARAM_NAME, LocalDateTime.parse(Optional.ofNullable(params.get("to")).orElseGet(LocalDateTime.MAX::toString)));
                        }})
                        .build())

                .withJoinCondition(JoinCondition.builder()
                        .comparison(Comparison.EQUAL)
                        .joinClass(InsightEvaluator.class)
                        .joinAttributeName("evaluator")
                        .field("questionId")
                        .value(params.get("question") != null ? Long.valueOf(params.get("question")) : null)
                        .build())

                .build();
    }


    private Long getQuestionId(ServletRequest request) {
        String qIdString = request.getParameter("question");
        Long qId = null;
        try {
            qId = Long.valueOf(qIdString);
        } catch (NumberFormatException nfe) {
            throw new BadRequestException("Bad question id " + qIdString, nfe);
        }
        return qId;
    }

    private EntityFilter.EntityFilterBuilder<Insight> addNonPredefinedParams(ServletRequest request) {
        EntityFilter.EntityFilterBuilder<Insight> builder = EntityFilter.<Insight>builder();

        getNotPredefinedParams(request).entrySet().stream()
                .forEach(e -> builder
                        .withJoinMapCondition(JoinMapCondition.builder()
                                .comparison(Comparison.EQUAL)
                                .field("parameters")
                                .key(e.getKey())
                                .value(e.getValue())
                                .build()));

        return builder;

    }

    Map<String, String> getPredefinedParams(ServletRequest request) {
        return request.getParameterMap().entrySet().stream()
                .filter(e -> PREDEFINED_REQUEST_PARAMS_NAMES.contains(e.getKey()))
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()[0]), Map::putAll);
    }

    Map<String, String> getNotPredefinedParams(ServletRequest request) {
        return request.getParameterMap().entrySet().stream()
                .filter(e -> !PREDEFINED_REQUEST_PARAMS_NAMES.contains(e.getKey()))
                .collect(HashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()[0]), Map::putAll);
    }
}
