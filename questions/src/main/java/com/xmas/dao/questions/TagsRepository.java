package com.xmas.dao.questions;


import com.xmas.entity.questions.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface TagsRepository extends CrudRepository<Tag, Integer>{

    @Query("SELECT tag FROM Tag tag WHERE tag.name = (:name)")
    Tag getByName(@Param("name") String name);
}
