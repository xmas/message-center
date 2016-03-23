package com.xmas.insight.dao;

import com.xmas.insight.entity.Insight;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface InsightRepository extends CrudRepository<Insight, Long>, JpaSpecificationExecutor<Insight> {

    @Query("SELECT insight FROM Insight insight")
    List<Insight> find();

    @Query("SELECT insight FROM Insight insight " +
            "INNER JOIN insight.evaluator evaluator " +
            "WHERE evaluator.questionId = ?1 ")
    List<Insight> findByQuestion( @Param("id")Long qId);

}
