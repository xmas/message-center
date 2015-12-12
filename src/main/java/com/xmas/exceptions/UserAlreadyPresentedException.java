package com.xmas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "User with such GUID is already presented.")
public class UserAlreadyPresentedException extends ConflictException{

    public UserAlreadyPresentedException(Long GUID) {
        super("User with such GUID(" + GUID + ") is already presented.");
    }
}
