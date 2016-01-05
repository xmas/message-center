package com.xmas.controller;

import com.xmas.entity.push.Device;
import com.xmas.entity.push.Medium;
import com.xmas.entity.push.User;
import com.xmas.notifiers.safari.SafariLogJSONEntity;
import com.xmas.notifiers.safari.pushpackage.ZipCreator;
import com.xmas.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Controller
public class SafariPushController {

    public static final String HEADER_PREFIX = "ApplePushNotifications ";

    private static final Logger logger = LogManager.getLogger("SafariPushLogger");

    @Autowired
    private ZipCreator zipCreator;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/v1/pushPackages/${safari.website.json.websitePushID}", method = RequestMethod.POST)
    public void pushPackages(@RequestBody User user, HttpServletResponse response) throws IOException {
        response.setHeader("Content-type", "application/zip");
        response.getOutputStream().write(zipCreator.create(user.getGuid()));
        logger.fatal("PushPackage for user " + user.getGuid());
    }

    @RequestMapping(value = "/v1/devices/{deviceToken}/registrations/${safari.website.json.websitePushID}", method = RequestMethod.POST)
    public void register(@PathVariable String deviceToken,
                         @RequestHeader("Authorization") String authorization,
                         HttpServletRequest request) {
        Long GUID = zipCreator.encodeUserGUID(authorization.replace(HEADER_PREFIX, ""));
        userService.addDevice(createSafariDevice(deviceToken), GUID, request.getRemoteAddr());
        logger.fatal("Registration for user " + GUID);
    }

    @RequestMapping(value = "/v1/devices/{deviceToken}/registrations/${safari.website.json.websitePushID}", method = RequestMethod.DELETE)
    public void delete(@PathVariable String deviceToken,
                         @RequestHeader("Authorization") String authorization) {
        Long GUID = zipCreator.encodeUserGUID(authorization);
        userService.deleteDevice(GUID, deviceToken);
    }

    @RequestMapping(value = "/v1/log", method = RequestMethod.POST)
    public void log(@RequestBody SafariLogJSONEntity logJSONEntity) {
        logger.warn(Arrays.toString(logJSONEntity.getLogs()));
    }

    private Device createSafariDevice(String token) {
        Device device = new Device();
        device.setMedium(new Medium(Medium.SAFARI));
        device.setToken(token);
        return device;
    }

}
