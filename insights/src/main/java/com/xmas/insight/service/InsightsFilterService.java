package com.xmas.insight.service;

import com.xmas.insight.entity.Insight;
import com.xmas.util.NonePredefinedAttributesConverter;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InsightsFilterService {

    private static final Set<String> PREDEFINED_REQUEST_PARAMS_NAMES = new HashSet<String>() {{
        add("question");
        add("eval");
        add("from");
        add("to");
    }};

    public Iterable<Insight> aplyFilters(ServletRequest request, Collection<Insight> data) {
        return aplyPredefinedRequestParamsFilters(request, addFilters(getNotPredefinedParams(request), data.stream()))
                .collect(Collectors.toList());
    }

    Stream<Insight> aplyPredefinedRequestParamsFilters(ServletRequest request, Stream<Insight> dataStream) {
        Map<String, String> params = getPredefinedParams(request);

        return dataStream.filter(i -> params.get("question") == null || i.getEvaluator().getQuestionId().equals(Long.valueOf(params.get("question"))))
                .filter(i -> params.get("eval") == null || i.getEvaluator().getId().equals(Long.valueOf(params.get("eval"))))
                .filter(i -> params.get("from") == null || i.getDate().isAfter(LocalDateTime.parse(params.get("from"))))
                .filter(i -> params.get("to") == null || i.getDate().isBefore(LocalDateTime.parse(params.get("to"))));
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

    <T extends Insight> Stream<T> addFilters(Map<String, String> params, Stream<T> stream) {
        Predicate<Insight> filter = params.entrySet().stream()
                .map(e -> buildPredicate(e.getKey(), e.getValue()))
                .reduce((Predicate<Insight>) (e -> true), Predicate::and, Predicate::and);
        return stream.filter(filter);
    }

    <T extends Insight> Predicate<T> buildPredicate(String paramName, String paramValue) {
        Object val = new NonePredefinedAttributesConverter().convertToEntityAttribute(paramValue);
        return insight -> {
            Map<String, Object> params = insight.getParameters();
            return params.containsKey(paramName) && params.get(paramName).equals(val);
        };
    }
}
