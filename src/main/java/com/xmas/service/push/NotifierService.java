package com.xmas.service.push;

import com.xmas.entity.push.Medium;
import com.xmas.entity.push.Message;
import com.xmas.service.push.notifiers.chrome.ChromeNotifier;
import com.xmas.service.push.notifiers.email.EmailNotifier;
import com.xmas.service.push.notifiers.safari.SafariNotifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
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
    EmailNotifier emailNotifier;

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
            case Medium.EMAIL: emailNotifier.pushMessage(message, tokens);break;
        }
    }

    protected long calculateDelay(ZonedDateTime time){
        return Duration.between(LocalDateTime.now(Clock.systemUTC()), time).getSeconds();
    }
}
