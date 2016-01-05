package com.xmas.service.questions;

import com.xmas.dao.questions.TagsRepository;
import com.xmas.entity.questions.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagsService {

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private TagsRepository tagsRepository;

    public void createTag(String name){
        tagsRepository.save(new Tag(name));
    }

    public Iterable<Tag> getTags(){
        return tagsRepository.findAll();
    }

    public Tag getTag(String name){
        return tagsRepository.getByName(name);
    }

    public Tag getTag(Integer id){
        return tagsRepository.findOne(id);
    }

    public void deleteTag(Integer id){
        tagsRepository.delete(id);
    }
}
