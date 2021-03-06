package com.xmas.controller.push;

import com.xmas.entity.push.Device;
import com.xmas.service.push.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users/{GUID}/devices")
public class DevicesController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public Iterable<Device> getDevices(@PathVariable Long GUID){
        return userService.getDevices(GUID);
    }

    @RequestMapping(value = "/{deviceId}", method = RequestMethod.GET)
    public Device getDevice(@PathVariable Long GUID, @PathVariable Integer deviceId){
        return userService.getDevice(GUID, deviceId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void addDevice(@PathVariable Long GUID, @RequestBody Device device, HttpServletRequest request){
        userService.addDevice(device, GUID, request.getRemoteAddr());
    }

    @RequestMapping(value = "/{token}", method = RequestMethod.DELETE)
    public void deleteDevice(@PathVariable Long GUID, @PathVariable String token){
        userService.deleteDevice(GUID, token);
    }

}
