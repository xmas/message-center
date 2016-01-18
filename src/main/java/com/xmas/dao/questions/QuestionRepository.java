package com.xmas.dao.questions;

import com.xmas.entity.questions.Question;
import com.xmas.entity.questions.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends CrudRepository<Question, Integer>{

    @Query("SELECT question from Question question WHERE question.id = ?1")
    Optional<Question> getById(Integer id);

    @Query("SELECT DISTINCT question from Question question INNER JOIN question.tags t WHERE t IN ?1")
    List<Question> getByTags(List<Tag> tags);

}
