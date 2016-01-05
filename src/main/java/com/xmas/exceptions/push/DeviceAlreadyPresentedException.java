package com.xmas.exceptions.push;

import com.xmas.exceptions.ConflictException;

public class DeviceAlreadyPresentedException extends ConflictException {
    public DeviceAlreadyPresentedException() {
        super("Device with such token already owned by other user.");
    }
}
