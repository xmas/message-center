package com.xmas.exceptions.R;

import com.xmas.exceptions.ProcessingException;

public class CantLoadScriptException extends ProcessingException {
    public CantLoadScriptException() {
        super("Can't load script.");
    }

    public CantLoadScriptException(String message) {
        super(message);
    }

    public CantLoadScriptException(String message, Throwable cause) {
        super(message, cause);
    }

    public CantLoadScriptException(Throwable cause) {
        super(cause);
    }

    public CantLoadScriptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
