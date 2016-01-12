package com.xmas.service.questions.datasource;

public enum DataSourceType {
    HTTPAPY("File that can be downloaded from external API"),
    FILE_UPLOAD("File that can be uploaded by calling this API");

    private String desc;

    private DataSourceType(String desc){
        this.desc = desc;
    }
}
