package com.xmas.exceptions;

public class NoSuchUserPresentedException extends RuntimeException{
    public NoSuchUserPresentedException(Long GUID) {
        super("User with such GUID(" + GUID + ") is not found.");
    }
}
