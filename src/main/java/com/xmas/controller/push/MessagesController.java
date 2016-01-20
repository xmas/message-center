package com.xmas.controller.push;

import com.xmas.entity.push.Message;
import com.xmas.service.push.MessagesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("")
public class MessagesController {

    @Autowired
    MessagesService messagesService;

    @RequestMapping(value = "users/{GUID}/messages/v1",method = RequestMethod.GET)
    public Collection<Message> getMessages(@RequestParam(required = false) LocalDateTime time, @PathVariable Long GUID){
        return messagesService.getMessages(GUID, message -> message.getCreated().isAfter(time));
    }

    @RequestMapping(value = "/messages/v1", method = RequestMethod.POST, consumes = "application/json")
    public void addMessage(@RequestBody List<Message> messages){
        messages.forEach(messagesService::addMessage);
    }

    @RequestMapping(value = "users/{GUID}/messages/v1/{id}",method = RequestMethod.DELETE)
    public void deleteMessage(@PathVariable Long GUID, @PathVariable Long id){
        messagesService.deleteMessage(id);
    }

    @RequestMapping(value = "users/{GUID}/messages/v1", method = RequestMethod.POST)
    public void read(@PathVariable Long GUID){
        messagesService.setRead(GUID);
    }

    @RequestMapping(value = "users/{GUID}/messages/v1/unread")
    public List<Message> getUnread(@PathVariable Long GUID){
        return messagesService.getUnread(GUID);
    }


}
