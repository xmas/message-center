package com.xmas.controller.push;

import com.xmas.entity.push.User;
import com.xmas.service.push.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public Iterable<User> getUsers(){
        return userService.getAll();
    }

    @RequestMapping(value = "/{GUID}", method = RequestMethod.GET, produces = "application/json")
    public User getUser(@PathVariable Long GUID){
        return userService.getUser(GUID);
    }

    @RequestMapping(value = "/{GUID}", method = RequestMethod.PUT, consumes = "application/json")
    public void addUser(@PathVariable Long GUID){
        userService.addUser(GUID);
    }

    @RequestMapping(value = "/{GUID}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable Long GUID){
        userService.deleteUser(GUID);
    }
}
