package com.xmas.exceptions.questions;

import com.xmas.exceptions.NotFoundException;

public class QuestionNotFoundException extends NotFoundException{
    public QuestionNotFoundException() {
        super("Cant find question with such id.");
    }
}
