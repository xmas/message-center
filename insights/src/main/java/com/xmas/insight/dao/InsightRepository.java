package com.xmas.insight.dao;

import com.xmas.insight.entity.Insight;
import com.xmas.insight.entity.InsightEvaluator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "insights", path = "insights")
public interface InsightRepository extends CrudRepository<Insight, Long>{

    @Query("SELECT insight FROM Insight insight WHERE insight.evaluator = ?1 AND insight.date > ?2")
    List<Insight> find(InsightEvaluator evaluator, LocalDateTime date);
}
