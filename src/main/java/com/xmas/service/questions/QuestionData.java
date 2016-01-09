package com.xmas.service.questions;

import com.xmas.service.questions.data.DataType;

@FunctionalInterface
public interface QuestionData<T>{

    default DataType getType(){
        return DataType.ABSTRACT_DATA;
    }

    T evaluateData(byte[] data);

}
