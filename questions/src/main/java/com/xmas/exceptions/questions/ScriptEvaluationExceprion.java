package com.xmas.exceptions.questions;

import com.xmas.exceptions.ProcessingException;

public class ScriptEvaluationExceprion extends ProcessingException {

    public ScriptEvaluationExceprion() {
    }

    public ScriptEvaluationExceprion(String message) {
        super(message);
    }

    public ScriptEvaluationExceprion(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptEvaluationExceprion(Throwable cause) {
        super(cause);
    }

    public ScriptEvaluationExceprion(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
