package com.xmas.controller;

import com.xmas.entity.Tag;
import com.xmas.service.TagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tags")
public class TagsController {

    @Autowired
    private TagsService tagsService;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Tag> getTags(){
        return tagsService.getTags();
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createTag(@RequestParam String name){
        tagsService.createTag(name);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteTag(@PathVariable Integer id){
        tagsService.deleteTag(id);
    }
}
