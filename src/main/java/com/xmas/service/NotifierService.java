package com.xmas.service;

import com.xmas.entity.Medium;
import com.xmas.entity.Message;
import com.xmas.notifiers.chrome.ChromeNotifier;
import com.xmas.notifiers.safari.SafariNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class NotifierService {

    @Autowired
    private ChromeNotifier chromeNotifier;

    @Autowired
    SafariNotifier safariNotifier;

    @Autowired
    ScheduledExecutorService executor;

    public void push(Medium medium, Message message, List<String> tokens){
        if(message.getPushTime() != null){
            postponedPush(medium, message, tokens);
        }else {
            immediatePush(medium, message, tokens);
        }
    }

    private void postponedPush(Medium medium, Message message, List<String> tokens){
        executor.schedule(() -> immediatePush(medium, message, tokens), calculateDelay(message.getPushTime()), TimeUnit.SECONDS );
    }

    private void immediatePush(Medium medium, Message message, List<String> tokens){
        switch (medium.getName()){
            case Medium.CHROME: chromeNotifier.pushMessage(message, tokens);break;
            case Medium.SAFARI: safariNotifier.pushMessage(message, tokens);break;
        }
    }

    /**
     * Calculte peirod in seconds between now and {@param time} in future
     * @return seconds between now and given time
     * @throws IllegalArgumentException if time is before now
     */
    private long calculateDelay(LocalDateTime time){
        if(time.isBefore(LocalDateTime.now())) throw new IllegalArgumentException("Specified time can't be before now.");
        return Duration.between(LocalDateTime.now(), time).getSeconds();
    }
}
