package com.xmas.controller;

import com.xmas.dao.MessageRepository;
import com.xmas.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequestMapping("/messages/v1")
public class PushController {

    @Autowired
    MessageRepository messageRepository;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Message> getMessages(){
        return new ArrayList<Message>();
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public void addMessage(@RequestBody Message message){
        messageRepository.save(message);
    }


}
