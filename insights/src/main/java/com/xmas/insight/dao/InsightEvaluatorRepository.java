package com.xmas.insight.dao;

import com.xmas.insight.entity.InsightEvaluator;
import com.xmas.util.ScheduledCrudRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Qualifier annotation is required since scheduler
 * in Common module require repository
 * that contains sheduled entities
 */
@Qualifier("evaluatedEntityRepository")
public interface InsightEvaluatorRepository extends ScheduledCrudRepository<InsightEvaluator> {

    @Query("SELECT insightEvaluator FROM InsightEvaluator insightEvaluator WHERE insightEvaluator.questionId = ?1")
    Iterable<InsightEvaluator> getByQuestionId(Long questionId);

    @Query("SELECT insightEvaluator FROM InsightEvaluator insightEvaluator WHERE insightEvaluator.cron <> null")
    List<InsightEvaluator> getScheduled();
}
