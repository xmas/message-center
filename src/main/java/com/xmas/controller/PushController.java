package com.xmas.controller;

import com.xmas.entity.Message;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/messages/v1")
public class PushController {

    @RequestMapping(method = RequestMethod.POST)
    public List<Message> getMessages(@RequestBody Message message){
        List<Message> result = new ArrayList<Message>();
        result.add(message);
        return result;
    }
}
