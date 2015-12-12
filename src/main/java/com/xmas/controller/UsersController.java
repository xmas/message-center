package com.xmas.controller;

import com.xmas.entity.User;
import com.xmas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/{GUID}")
public class UsersController {

    @Autowired
    UserService userService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public User getUser(@PathVariable Long GUID){
        return userService.getUser(GUID);
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "application/json")
    public void addUser(@PathVariable Long GUID){
        userService.addUser(GUID);
    }


}
