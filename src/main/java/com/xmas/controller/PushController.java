package com.xmas.controller;

import com.xmas.dao.MessageRepository;
import com.xmas.entity.Message;
import com.xmas.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping("/messages/v1")
public class PushController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessagesService messagesService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Message> getMessages(@RequestParam LocalDateTime time, Principal user){
        return messagesService.getMessages(user.getName(), message -> message.getCreated().isAfter(time));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public void addMessage(@RequestBody Message message){
        messageRepository.save(message);
    }


}
