package com.xmas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
public class NoSuchUserFoundException extends NotFoundException{
    public NoSuchUserFoundException(Long GUID) {
        super("User with such GUID(" + GUID + ") is not found.");
    }

    public NoSuchUserFoundException(String name) {
        super("User with such GUID(" + name + ") is not found.");
    }
}
