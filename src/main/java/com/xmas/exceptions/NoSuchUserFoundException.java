package com.xmas.exceptions;

public class NoSuchUserFoundException extends RuntimeException{
    public NoSuchUserFoundException(Long GUID) {
        super("User with such GUID(" + GUID + ") is not found.");
    }

    public NoSuchUserFoundException(String name) {
        super("User with such GUID(" + name + ") is not found.");
    }
}
