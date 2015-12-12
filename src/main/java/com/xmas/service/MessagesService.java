package com.xmas.service;

import com.xmas.dao.MediumsRepository;
import com.xmas.entity.Medium;
import com.xmas.entity.Message;
import com.xmas.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Message> getMessages(String userName, Predicate<Message> filter){
        List<Message> result = new ArrayList<>();
        userService.getUser(userName).getMessages().stream().filter(filter).forEach(result::add);
        return result;
    }

    public void processMessage(Message message){
        Set<Medium> mediums;
        if(message.getMediums() == null || message.getMediums().isEmpty()){
            mediums = new HashSet<>();
            mediums.addAll(mediumsRepository.getAll());
        }else {
            mediums = message.getMediums();
        }

        List<User> users = new ArrayList<>();
        message.getUsers().stream().forEach(user -> users.add(userService.getUser(user.getGUID())));
        if(users.isEmpty()) users.addAll(userService.getAll());

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
