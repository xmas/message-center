package com.xmas.notifiers.chrome;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.entity.push.Message;
import com.xmas.exceptions.NotificationSendingException;
import com.xmas.notifiers.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChromeNotifier implements Notifier {

    public static final String HTTP_METHOD = "POST";

    @Autowired
    private MessageSanderBuilder messageSanderBuilder;

    @Value("${google.chrome.push.api.key}")
    private String googleApiKey;

    @Value("${google.chrome.push.api.url}")
    private String googlePushAPIUrl;

    @Override
    public void pushMessage(Message message, List<String> tokens) {
        ISender sender = messageSanderBuilder.createSender(googlePushAPIUrl, prepareHeaders(), HTTP_METHOD);
        sender.send(prepareMessage(tokens));

    }

    public String prepareMessage(List<String> tokens){
        ObjectMapper mapper = new ObjectMapper();
        ChromeMessage chromeMessage = new ChromeMessage();
        chromeMessage.setRegistration_ids(tokens);

        try {
            return mapper.writeValueAsString(chromeMessage);
        } catch (JsonProcessingException e) {
            throw new NotificationSendingException(e.getMessage());
        }
    }

    public Map<String, String> prepareHeaders(){
        Map<String, String> headers = new HashMap<>();

        headers.put("Authorization", "key=" + googleApiKey);
        headers.put("Content-Type", "application/json");

        return headers;
    }
}
