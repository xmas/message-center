package com.xmas.controller;

import com.xmas.exceptions.ConflictException;
import com.xmas.exceptions.NotFoundException;
import com.xmas.exceptions.ProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleException(NotFoundException exception){
        logger.error(exception.getMessage());
        logger.debug(exception.getMessage(), exception);
        return exception.getMessage();
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public String handleException(ConflictException exception){
        logger.error(exception.getMessage());
        logger.debug(exception.getMessage(), exception);
        return exception.getMessage();
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(ProcessingException exception){
        logger.error(exception.getMessage());
        logger.debug(exception.getMessage(), exception);
        return exception.getMessage();
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception exception){
        logger.error(exception.getMessage());
        logger.debug(exception.getMessage(), exception);
        return exception.getMessage();
    }
}
