package com.xmas.dao.questions;


import com.xmas.entity.questions.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface TagsRepository extends CrudRepository<Tag, Integer>{

    @Query("SELECT tag FROM Tag tag WHERE tag.name = ?1")
    public Tag getByName(String name);
}
