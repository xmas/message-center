package com.xmas.controller;

import com.xmas.entity.Device;
import com.xmas.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/{GUID}/devices")
public class DevicesController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/{deviceId}", method = RequestMethod.GET)
    public Device getDevice(@PathVariable Long GUID, @PathVariable Integer deviceId){
        return userService.getDevice(GUID, deviceId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addDevice(@PathVariable Long GUID, @RequestBody Device device){
        userService.addDevice(device, GUID);
    }

}
