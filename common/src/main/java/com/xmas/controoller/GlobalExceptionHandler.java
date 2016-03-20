package com.xmas.controoller;

import com.xmas.exceptions.ConflictException;
import com.xmas.exceptions.NotFoundException;
import com.xmas.exceptions.ProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleException(NotFoundException exception){
        return logAndReturn(exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public String handleException(ConflictException exception){
        return logAndReturn(exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public String handleException(ConstraintViolationException exception){
        return logAndReturn(exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(ProcessingException exception){
        return logAndReturn(exception);
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception exception){
        return logAndReturn(exception);
    }

    private String logAndReturn(Throwable exception){
        log.error(exception.getMessage(), exception);
        return exception.getMessage();
    }
}
