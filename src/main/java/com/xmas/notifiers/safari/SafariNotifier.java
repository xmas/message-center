package com.xmas.notifiers.safari;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.xmas.entity.Message;
import com.xmas.notifiers.Notifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.Security;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class SafariNotifier implements Notifier{

    @Value("${safari.push.provider.url}")
    private String applePushProviderUrl;

    @Value("${safari.push.provider.port}")
    private int port;

    static {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
    }

    @Override
    public void pushMessage(Message message, List<String> tokens) {
        try {
            SSLSocketFactory factory =
                    (SSLSocketFactory) SSLSocketFactory.getDefault();

            
            SSLSocket socket =
                    (SSLSocket)factory.createSocket(applePushProviderUrl, port);

            socket.addHandshakeCompletedListener(handshakeCompletedEvent -> {

            });

            socket.getOutputStream().write();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] prepareMessage(Message message, List<String> tokens) throws IOException {
        byte[] items = prepareItems(message, tokens);

        byte[] messageBytes = new byte[items.length + 5];

        messageBytes[0] = 2;
        byte[] itemsLength = ByteBuffer.allocate(2).putShort((short)items.length).array();
        System.arraycopy(itemsLength, 0, messageBytes, 1, 4);

        System.arraycopy(items, 0, message, 5, items.length);

        return messageBytes;
    }

    private byte[] prepareItems(Message message, List<String> tokens) throws IOException {

        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        for (int i = 0; i < tokens.size(); i++) {
            byte[] item = prepareItem((byte)i, message, tokens.get(i));
            byteStream.write(item);
        }

        return byteStream.toByteArray();
    }

    private byte[] prepareItem(byte itemId, Message message, String token){
        byte[] itemData = prepareItemData(message, token);
        byte[] item = new byte[itemData.length + 3];

        item[0] = itemId;

        byte[] dataLength = ByteBuffer.allocate(2).putShort((short)item.length).array();
        System.arraycopy(dataLength, 0, item, 1, 2);

        System.arraycopy(itemData, 0, item, 3, itemData.length);

        return item;
    }

    private byte[] prepareItemData(Message message, String token){
        int position = 0;

        byte[] payload = preparePayload(message);

        byte[] item = new byte[payload.length + 41];

        byte[] tokenBytes = token.getBytes();
        assert tokenBytes.length == 32;
        System.arraycopy(tokenBytes, 0, item, position, 32);
        position += 32;

        System.arraycopy(payload, 0, item, position, payload.length);
        position += payload.length;

        //TODO Notification identifier should be there 4 bytes
        position += 4;

        int timestamp = (int) message.getExpiration().toEpochSecond(ZoneOffset.UTC);
        byte[] timeArray = ByteBuffer.allocate(4).putInt(timestamp).array();
        System.arraycopy(timeArray, 0, item, position, 4);

        //Set priority 10 - immediately, 5 - at a time that conserves power on the device receiving it
        item[item.length-1] = 10;
        return item;

    }

    private byte[] preparePayload(Message message){
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode objectNode = mapper.createObjectNode();

        ObjectNode alert = mapper.createObjectNode();
        alert.put("title", message.getTitle())
                .put("body", message.getMessage())
                .put("action", "View");

        ArrayNode urlArgs = mapper.createArrayNode();
        urlArgs.add("sdfg");

        ObjectNode aps = mapper.createObjectNode();
        aps.set("alert", alert);
        aps.set("url-args", urlArgs);

        objectNode.set("aps", aps);

        String payloadString = objectNode.toString();

        byte[] payload = payloadString.getBytes();
        byte[] nullTerminatedPayload = new byte[payload.length + 1];
        System.arraycopy(payload, 0, nullTerminatedPayload, 0, payload.length);
        return nullTerminatedPayload;
    }
}
