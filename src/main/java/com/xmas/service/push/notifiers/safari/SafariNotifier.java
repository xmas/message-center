package com.xmas.service.push.notifiers.safari;

import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsService;
import com.notnoop.apns.ReconnectPolicy;
import com.xmas.entity.push.Message;
import com.xmas.service.push.notifiers.Notifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static com.xmas.util.FileUtil.getFullPathFileName;

@Service
public class SafariNotifier implements Notifier{
    @Value("${safari.signature.password}")
    private String password;

    private ApnsService service;
    @PostConstruct
    public void init(){
        service = APNS.newService()
                .withAppleDestination(true)
                .withCert(getFullPathFileName("safari/ca.p12"), password)
                .withReconnectPolicy(ReconnectPolicy.Provided.EVERY_HALF_HOUR)
                .asPool(5)
                .build();
        service.start();
    }

    @Override
    public void pushMessage(Message message, List<String> tokens) {
        service.push(tokens, prepareMessage(message), new Date(message.getExpiration().toEpochSecond(ZoneOffset.UTC)));
    }

    private String prepareMessage(Message message){
        return APNS.newPayload()
                .alertAction("View")
                .alertBody(message.getMessage())
                .alertTitle(message.getTitle())
                .build();
    }

}
