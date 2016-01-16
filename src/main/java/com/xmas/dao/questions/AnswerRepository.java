package com.xmas.dao.questions;

import com.xmas.entity.questions.Answer;
import com.xmas.entity.questions.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AnswerRepository extends CrudRepository<Answer, Long>{

    @Query("SELECT answer FROM Answer answer WHERE answer.question = ?1 AND answer.date = ?2")
    Optional<Answer> findAnswer(Question question, LocalDate date);
}
