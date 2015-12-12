package com.xmas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class NotificationSendingException extends ProcessingException{
    public NotificationSendingException(String message) {
        super(message);
    }
}
