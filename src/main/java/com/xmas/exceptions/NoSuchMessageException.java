package com.xmas.exceptions;

public class NoSuchMessageException extends NotFoundException {
    public NoSuchMessageException(Long id) {
        super("Message with ID(" + id + ") is not found.");
    }
}
