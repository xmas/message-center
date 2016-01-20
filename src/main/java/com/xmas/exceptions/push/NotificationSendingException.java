package com.xmas.exceptions.push;

import com.xmas.exceptions.ProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class NotificationSendingException extends ProcessingException {
    public NotificationSendingException(String message) {
        super(message);
    }

    public NotificationSendingException(Throwable cause) {
        super(cause);
    }

    public NotificationSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
