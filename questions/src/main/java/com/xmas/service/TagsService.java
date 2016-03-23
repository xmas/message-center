package com.xmas.service;

import com.xmas.dao.TagsRepository;
import com.xmas.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public void deleteTag(Integer id){
        tagsRepository.delete(id);
    }

    public List<Tag> mapTagsToEntitiesFromDB(List<String> tags){
        return tags.stream()
                .map(this::getTag)
                .filter(tag -> tag != null)
                .collect(Collectors.toList());
    }

    public static class TagConverter implements Converter<String, Tag> {
        @Override
        public Tag convert(String source) {
            return new Tag(source);
        }
    }
}
