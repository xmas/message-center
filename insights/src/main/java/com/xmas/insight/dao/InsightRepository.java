package com.xmas.insight.dao;

import com.xmas.insight.entity.Insight;
import com.xmas.insight.entity.InsightEvaluator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InsightRepository extends CrudRepository<Insight, Long>{

    @Query("SELECT insight FROM Insight insight WHERE insight.evaluator = ?1 AND insight.date > ?2")
    List<Insight> find(InsightEvaluator evaluator, LocalDateTime date);
}
