package com.xmas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Device not found.")
public class NoSuchDeviceFound extends NotFoundException{
    public NoSuchDeviceFound(Integer id) {
        super("Device with id " + id + " not found.");
    }
}
