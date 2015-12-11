package com.xmas.exceptions;

public class NotificationSendingException extends RuntimeException{
    public NotificationSendingException(String message) {
        super(message);
    }
}
