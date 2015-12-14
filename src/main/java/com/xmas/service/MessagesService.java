package com.xmas.service;

import com.xmas.dao.MediumsRepository;
import com.xmas.dao.MessageRepository;
import com.xmas.entity.Medium;
import com.xmas.entity.Message;
import com.xmas.entity.User;
import com.xmas.exceptions.NoSuchMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

@Service
public class MessagesService {

    @Autowired
    UserService userService;

    @Autowired
    NotifierService notifierService;

    @Autowired
    MediumsRepository mediumsRepository;

    @Autowired
    MessageRepository messageRepository;

    public List<Message> getMessages(Long GUID, Predicate<Message> filter) {
        List<Message> result = new ArrayList<>();
        userService.getUser(GUID).getMessages().stream().filter(filter).forEach(result::add);
        return result;
    }

    public List<Message> getUnread(Long GUID){
        User user = userService.getUser(GUID);
        ArrayList<Message> result = new ArrayList<>();
        user.getMessages().stream().filter(message -> !message.isAccepted()).forEach(result::add);
        return result;
    }

    public void setRead(Long id){
        Message message = messageRepository.get(id).orElseThrow(() -> new NoSuchMessageException(id));
        message.setAccepted(true);
        messageRepository.save(message);
    }

    public void addMessage(Message message) {
        List<User> users = new ArrayList<>();

        if (message.getUsers() != null)
            message.getUsers().forEach(user -> {
                User savedUser = userService.getUser(user.getGuid());
                users.add(savedUser);
            });

        if(message.getUsers() == null || message.getUsers().isEmpty()){
            users.addAll(userService.getAll());
        }

        message.setUsers(users);

        message.setCreated(LocalDateTime.now());

        messageRepository.save(message);

        processMessage(message);

    }

    private void processMessage(Message message) {
        Set<Medium> mediums;
        if (message.getMediums() == null || message.getMediums().isEmpty()) {
            mediums = new HashSet<>();
            mediums.addAll(mediumsRepository.getAll());
        } else {
            mediums = message.getMediums();
        }

        List<User> users = new ArrayList<>();
        message.getUsers().stream().forEach(user -> users.add(userService.getUser(user.getGuid())));
        if (users.isEmpty()) users.addAll(userService.getAll());

        Map<Medium, List<String>> tokens = new HashMap<>();


        mediums.forEach(medium1 -> {
            List<String> list = new ArrayList<>();

            users.forEach(user -> user.getDevices().stream().filter(device -> device.getMedium().equals(medium1)).
                    forEach(device -> list.add(device.getToken())));

            tokens.put(medium1, list);
        });

        tokens.forEach((medium, tokenList) -> {
            notifierService.push(medium, message, tokenList);
        });

    }

}
