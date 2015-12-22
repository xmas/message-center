package com.xmas.service;

import com.xmas.entity.Medium;
import com.xmas.entity.Message;
import com.xmas.notifiers.chrome.ChromeNotifier;
import com.xmas.notifiers.safari.SafariNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class NotifierService {

    @Autowired
    private ChromeNotifier chromeNotifier;

    @Autowired
    SafariNotifier safariNotifier;

    @Autowired
    ScheduledExecutorService executor;

    public void push(Medium medium, Message message, List<String> tokens){
        switch (medium.getName()){
            case Medium.CHROME: chromeNotifier.pushMessage(message, tokens);break;
            case Medium.SAFARI: safariNotifier.pushMessage(message, tokens);break;
        }
    }

    public void postponedPush(Medium medium, Message message, List<String> tokens, LocalDateTime time){
        executor.schedule()
    }
}
