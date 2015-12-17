package com.xmas.controller;

import com.xmas.notifiers.safari.ZipCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class SafariPushController {

    @Autowired
    private ZipCreator zipCreator;

    @RequestMapping(value = "/v1/pushPackages/${safari.website.json.websitePushID}", method = RequestMethod.GET)
    public void pushPackages(HttpServletResponse response) throws IOException {
        response.setHeader("Content-type", "application/zip");
        response.getOutputStream().write(zipCreator.create(123456L));
    }

    @RequestMapping(value = "/v1/devices/{deviceToken}/registrations/${safari.website.json.websitePushID}", method = RequestMethod.POST)
    public void register(@PathVariable String deviceToken){

    }

}
