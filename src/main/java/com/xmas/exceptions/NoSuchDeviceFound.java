package com.xmas.exceptions;

public class NoSuchDeviceFound extends RuntimeException{
    public NoSuchDeviceFound(Integer id) {
        super("Device with id " + id + " not found.");
    }
}
