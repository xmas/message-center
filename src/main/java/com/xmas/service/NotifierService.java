package com.xmas.service;

import com.xmas.entity.Medium;
import com.xmas.entity.Message;
import com.xmas.notifiers.chrome.ChromeNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotifierService {

    @Autowired
    ChromeNotifier chromeNotifier;

    public void push(Medium medium, Message message, List<String> tokens){
        switch (medium.getName()){
            case "chrome": chromeNotifier.pushMessage(message, tokens);break;
        }
    }
}
