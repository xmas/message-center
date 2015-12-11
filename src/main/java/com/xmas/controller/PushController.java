package com.xmas.controller;

import com.xmas.dao.MessageRepository;
import com.xmas.entity.Message;
import com.xmas.service.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequestMapping("users{GUID}/messages/v1")
public class PushController {

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    MessagesService messagesService;

    @RequestMapping(method = RequestMethod.GET)
    public Collection<Message> getMessages(@PathVariable Long GUID, @RequestParam LocalDateTime time){
        return messagesService.getMessages(GUID, message -> message.getCreated().isAfter(time));
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public void addMessage(@RequestBody Message message){
        messageRepository.save(message);
    }


}
