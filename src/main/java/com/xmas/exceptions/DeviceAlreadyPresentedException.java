package com.xmas.exceptions;

public class DeviceAlreadyPresentedException extends ConflictException{
    public DeviceAlreadyPresentedException() {
        super("Device with such token already owned by other user.");
    }
}
