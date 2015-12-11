package com.xmas.exceptions;

public class UserAlreadyPresentedException extends RuntimeException{

    public UserAlreadyPresentedException(Long GUID) {
        super("User with such GUID(" + GUID + ") is already presented.");
    }
}
