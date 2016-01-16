package com.xmas.dao.questions;

import com.xmas.entity.questions.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface QuestionRepository extends CrudRepository<Question, Integer>{

    @Query("SELECT question from Question question WHERE question.id = ?1")
    Optional<Question> getById(Integer id);

}
