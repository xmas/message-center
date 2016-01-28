package com.xmas.exceptions.R;

import com.xmas.exceptions.ProcessingException;

public class DirCreationException extends ProcessingException {
    public DirCreationException() {
    }

    public DirCreationException(String message) {
        super(message);
    }

    public DirCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DirCreationException(Throwable cause) {
        super(cause);
    }

    public DirCreationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
