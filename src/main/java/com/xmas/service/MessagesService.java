package com.xmas.service;

import com.xmas.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Service
public class MessagesService {

    @Autowired
    UserService userService;

    public List<Message> getMessages(String userName, Predicate<Message> filter){
        List<Message> result = new ArrayList<>();
        userService.getUser(userName).getMessages().stream().filter(filter).forEach(result::add);
        return result;
    }

    public void processMessage(Message message){

    }

}
