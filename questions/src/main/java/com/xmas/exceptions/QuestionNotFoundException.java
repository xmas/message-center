package com.xmas.exceptions;

import com.xmas.exceptions.NotFoundException;

public class QuestionNotFoundException extends NotFoundException{
    public QuestionNotFoundException() {
        super("Cant find question with such id.");
    }
}
