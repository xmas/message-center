package com.xmas.dao;

import com.xmas.entity.Answer;
import com.xmas.entity.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AnswerRepository extends CrudRepository<Answer, Long>{

    @Query("SELECT answer FROM Answer answer WHERE answer.question = ?1 AND answer.date > ?2")
    List<Answer> findAnswers(Question question, LocalDateTime date);

    @Query("SELECT answer FROM Answer answer WHERE answer.question = ?1 AND (answer.date BETWEEN ?2 AND ?3)")
    List<Answer> findAnswers(Question question, LocalDateTime from, LocalDateTime to);
}
