package com.xmas.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.xmas.entity.User;
import com.xmas.entity.UserMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserMessageJsonDeserializer extends JsonDeserializer<List<UserMessage>> {

    @Override
    public List<UserMessage> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        ObjectCodec userCodec = jsonParser.getCodec();
        JsonNode userMessagesNode = userCodec.readTree(jsonParser);

        List<UserMessage> userMessages = new ArrayList<>();

        userMessagesNode.iterator().forEachRemaining(userNode -> {
            UserMessage userMessage = new UserMessage();
            Long guid = userNode.get("guid").asLong();
            User user = new User();
            user.setGuid(guid);
            userMessage.setUser(user);
            userMessages.add(userMessage);
        });


        return userMessages;
    }
}
