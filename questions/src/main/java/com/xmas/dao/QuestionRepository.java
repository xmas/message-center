package com.xmas.dao;

import com.xmas.entity.Question;
import com.xmas.entity.Tag;
import com.xmas.service.ScheduledCrudRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Qualifier("evaluatedEntityRepository")
public interface QuestionRepository extends ScheduledCrudRepository<Question> {

    @Query("SELECT question from Question question")
    List<Question> getAll();

    @Query("SELECT question from Question question WHERE question.id = ?1")
    Optional<Question> getById(Long id);

    @Query("SELECT DISTINCT question from Question question INNER JOIN question.tags t WHERE t IN ?1")
    List<Question> getByTags(List<Tag> tags);

    @Query("SELECT question FROM Question question WHERE question.cron <> null")
    List<Question> getScheduled();

}
