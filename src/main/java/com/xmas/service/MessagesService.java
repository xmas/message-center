package com.xmas.service;

import com.xmas.dao.MediumsRepository;
import com.xmas.dao.MessageRepository;
import com.xmas.dao.UserMessageRepository;
import com.xmas.entity.*;
import com.xmas.exceptions.NoSuchMessageException;
import com.xmas.exceptions.NoSuchUserFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class MessagesService {

    @Autowired
    private UserService userService;

    @Autowired
    private NotifierService notifierService;

    @Autowired
    private MediumsRepository mediumsRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserMessageRepository userMessageRepository;


    public List<Message> getMessages(Long GUID, Predicate<Message> filter) {
        List<Message> result = new ArrayList<>();
        userService.getUser(GUID).getUserMessages().stream().map(UserMessage::getMessage).filter(filter).forEach(result::add);
        return result;
    }

    public List<Message> getUnread(Long GUID) {
        return userService.getUser(GUID)
                .getUserMessages().stream()
                .filter(message -> !message.isAccepted())
                .map(UserMessage::getMessage)
                .collect(Collectors.toList());
    }

    public void setRead(Long guid, Long id) {
        Message message = messageRepository.get(id).orElseThrow(() -> new NoSuchMessageException(id));
        userMessageRepository.save(message.getUserMessages().stream()
                .filter(userMessage -> userMessage.getUser().getGuid().equals(guid))
                .peek(userMessage1 -> userMessage1.setAccepted(true))
                .collect(Collectors.toList()));
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
        if (message.getUserMessages() == null || message.getUserMessages().isEmpty()) {
            message.setUserMessages(filterUsersThatDoNotHasRequiredMediums(message, userService.getAll()));
        } else {
            message.setUserMessages(filterUsersThatDoNotHasRequiredMediums(message, message.getUserMessages().stream()
                    .map(UserMessage::getUser)
                    .map(User::getGuid)
                    .<User>map(guid -> {
                        try {
                            return userService.getUser(guid);
                        } catch (NoSuchUserFoundException e){
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

            message.getUserMessages().stream()
                    .map(UserMessage::getUser)
                    .forEach(user -> user.getDevices().stream()
                            .filter(device -> device.getMedium().equals(medium))
                            .forEach(device -> list.add(device.getToken())));

            if (!list.isEmpty())
                tokens.put(medium, list);
        });

        if (!tokens.isEmpty()) {
            messageRepository.save(message);
            message.getUserMessages().stream().forEach(userMessageRepository::save);

            tokens.forEach((medium, tokenList) -> {
                notifierService.push(medium, message, tokenList);
            });
        }

    }

}
