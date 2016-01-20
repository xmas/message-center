package com.xmas.exceptions.push;

import com.xmas.exceptions.NotFoundException;

public class NoSuchMessageException extends NotFoundException {
    public NoSuchMessageException(Long id) {
        super("Message with ID(" + id + ") is not found.");
    }
}
