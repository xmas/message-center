package com.xmas.service;

import com.xmas.dao.MediumsRepository;
import com.xmas.dao.MessageRepository;
import com.xmas.entity.Device;
import com.xmas.entity.Medium;
import com.xmas.entity.Message;
import com.xmas.entity.User;
import com.xmas.exceptions.NoSuchMessageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public List<Message> getUnread(Long GUID) {
        User user = userService.getUser(GUID);
        ArrayList<Message> result = new ArrayList<>();
        user.getMessages().stream().filter(message -> !message.isAccepted()).forEach(result::add);
        return result;
    }

    public void setRead(Long id) {
        Message message = messageRepository.get(id).orElseThrow(() -> new NoSuchMessageException(id));
        message.setAccepted(true);
        messageRepository.save(message);
    }

    public void addMessage(Message message) {
        message.setCreated(LocalDateTime.now());

        processMessage(populateUsers(populateMediums(message)));

    }

    private Message populateMediums(Message message) {
        if (message.getMediums() == null || message.getMediums().isEmpty()) {
            message.setMediums(mediumsRepository.getAll());
        }
        return message;
    }

    private Message populateUsers(Message message) {
        if (message.getUsers() == null || message.getUsers().isEmpty()) {
            message.setUsers(filterUsersThatDoNotHasRequiredMediums(message, userService.getAll()));
        } else {
            message.setUsers(filterUsersThatDoNotHasRequiredMediums(message, message.getUsers().stream()
                    .map(User::getGuid)
                    .<User>map(userService::getUser)
                    .collect(Collectors.toList())));
        }
        return message;
    }

    private List<User> filterUsersThatDoNotHasRequiredMediums(Message message, List<User> users) {
        return users.stream()
                .filter(user -> hasUserRequiredMediums(user, message))
                .peek(user -> addMessageToUser(user, message))
                .collect(Collectors.toList());
    }

    private boolean hasUserRequiredMediums(User user, Message message){
        return user.getDevices().stream()
                .<Medium>map(Device::getMedium)
                .filter(message.getMediums()::contains).count() > 0;
    }

    private void addMessageToUser(User user, Message message){
        if (user.getMessages() != null)
            user.getMessages().add(message);
        else
            user.setMessages(new ArrayList<Message>() {{
                add(message);
            }});
    }

    private void processMessage(Message message) {

        Map<Medium, List<String>> tokens = new HashMap<>();

        message.getMediums().forEach(medium -> {
            List<String> list = new ArrayList<>();

            message.getUsers().forEach(user -> user.getDevices().stream().filter(device -> device.getMedium().equals(medium)).
                    forEach(device -> list.add(device.getToken())));

            if (!list.isEmpty())
                tokens.put(medium, list);
        });

        if (!tokens.isEmpty()) {
            messageRepository.save(message);

            tokens.forEach((medium, tokenList) -> {
                notifierService.push(medium, message, tokenList);
            });
        }

    }

}
