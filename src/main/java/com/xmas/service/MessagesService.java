package com.xmas.service;

import com.xmas.dao.MediumsRepository;
import com.xmas.dao.MessageRepository;
import com.xmas.dao.UserMessageRepository;
import com.xmas.entity.push.*;
import com.xmas.exceptions.NoSuchUserFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class MessagesService {

    @Autowired
    private UserService userService;

    @Autowired
    private NotifierService notifierService;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private MediumsRepository mediumsRepository;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private MessageRepository messageRepository;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private UserMessageRepository userMessageRepository;


    public List<Message> getMessages(Long GUID, Predicate<Message> filter) {
        return userService.getUser(GUID)
                .getUserMessages().stream()
                .map(UserMessage::getMessage)
                .filter(filter)
                .sorted((m1, m2) -> m1.getCreated().isBefore(m2.getCreated()) ? -1 : 1)
                .collect(Collectors.toList());
    }

    public void deleteMessage(Long id){
        messageRepository.delete(id);
    }

    public List<Message> getUnread(Long GUID) {
        return userService.getUser(GUID)
                .getUserMessages().stream()
                .filter(message -> !message.isAccepted())
                .map(UserMessage::getMessage)
                .collect(Collectors.toList());
    }

    public void setRead(Long guid) {
        userMessageRepository.save(userMessageRepository.getUnRead(guid).stream()
                .peek(userMessage -> userMessage.setAccepted(true))
                .collect(Collectors.toList()));
    }

    public void addMessage(Message message) {
        message.setCreated(LocalDateTime.now());

        saveMessage(message);
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
                    .map(UserMessage::getUser)
                    .map(User::getGuid)
                    .<User>map(guid -> {
                        try {
                            return userService.getUser(guid);
                        } catch (NoSuchUserFoundException e) {
                            return null;
                        }
                    })
                    .filter(user -> user != null)
                    .collect(Collectors.toList())));
        }
        return message;
    }

    private List<UserMessage> filterUsersThatDoNotHasRequiredMediums(Message message, List<User> users) {
        return users.stream()
                .filter(user -> hasUserRequiredMediums(user, message))
                .map(user -> {
                    UserMessage userMessage = new UserMessage();
                    userMessage.setMessage(message);
                    userMessage.setUser(user);
                    return userMessage;
                })
                .peek(userMessage -> addMessageToUser(userMessage.getUser(), userMessage))
                .collect(Collectors.toList());
    }

    private boolean hasUserRequiredMediums(User user, Message message) {
        return user.getDevices().stream()
                .<Medium>map(Device::getMedium)
                .filter(message.getMediums()::contains).count() > 0;
    }

    private void addMessageToUser(User user, UserMessage message) {
        if (user.getUserMessages() != null)
            user.getUserMessages().add(message);
        else
            user.setUserMessages(new ArrayList<UserMessage>() {{
                add(message);
            }});
    }

    private void processMessage(Message message) {

        Map<Medium, List<String>> tokens = new HashMap<>();

        message.getMediums().forEach(medium -> {
            List<String> list = new ArrayList<>();

            if(medium.getName().equals(Medium.EMAIL) && message.getEmails() != null && !message.getEmails().isEmpty()) {
                list.addAll(message.getEmails());
            }else {
                message.getUsers().stream()
                        .map(UserMessage::getUser)
                        .forEach(user -> user.getDevices().stream()
                                .filter(device -> device.getMedium().equals(medium))
                                .forEach(device -> list.add(device.getToken())));
            }

            if (!list.isEmpty())
                tokens.put(medium, list);
        });

        tokens.forEach((medium, tokenList) -> notifierService.push(medium, message, tokenList));
    }

    private void saveMessage(Message message){
        messageRepository.save(message);
        if(message.getUsers() == null || message.getUsers().isEmpty()){
            userService.getAll().stream()
                    .map(user -> new UserMessage(user, message))
                    .forEach(userMessageRepository::save);
        }else {
            message.getUsers().stream()
                    .peek(userMessage -> userMessage.setUser(userService.getUser(userMessage.getUser().getGuid())))
                    .peek(userMessage -> userMessage.setMessage(message))
                    .forEach(userMessageRepository::save);
        }

    }

}
