package com.xmas.insight.dao;

import com.xmas.insight.entity.Insight;
import com.xmas.insight.entity.InsightEvaluator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(collectionResourceRel = "insights", path = "insights")
public interface InsightRepository extends CrudRepository<Insight, Long>{

    @Query("SELECT insight FROM Insight insight WHERE insight.evaluator = ?1 AND insight.date > ?2")
    @RestResource(exported = false)
    List<Insight> find(InsightEvaluator evaluator, LocalDateTime date);

    @RestResource(path = "question", rel="question")
    @Query("SELECT insight FROM Insight insight " +
            "INNER JOIN insight.evaluator evaluator " +
            "WHERE evaluator.questionId = ?1 ")
    List<Insight> findByQuestion( @Param("id")Long qId);

    @Override
    @RestResource(exported = false)
    void delete(Long id);

    @Override
    @RestResource(exported = false)
    void delete(Insight insight);

    @Override
    @RestResource(exported = false)
    void delete(Iterable<? extends Insight> insights);

    @Override
    @RestResource(exported = false)
    <T extends Insight> T save(T insight);

    @Override
    @RestResource(exported = false)
    <T extends Insight> Iterable<T> save(Iterable<T> insights);
}
