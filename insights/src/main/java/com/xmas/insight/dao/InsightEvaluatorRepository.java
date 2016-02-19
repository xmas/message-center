package com.xmas.insight.dao;

import com.xmas.insight.entity.InsightEvaluator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface InsightEvaluatorRepository extends CrudRepository<InsightEvaluator, Long> {

    @Query("SELECT insightEvaluator FROM InsightEvaluator insightEvaluator WHERE insightEvaluator.questionId = ?1")
    Iterable<InsightEvaluator> getByQuestionId(Long questionId);
}
