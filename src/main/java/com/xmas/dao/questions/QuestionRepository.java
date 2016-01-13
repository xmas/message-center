package com.xmas.dao.questions;

import com.xmas.entity.questions.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepository extends CrudRepository<Question, Integer>{
}
