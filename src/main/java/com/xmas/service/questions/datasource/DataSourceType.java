package com.xmas.service.questions.datasource;

import org.springframework.web.multipart.MultipartFile;

public enum DataSourceType {
    HTTP_APY(String.class, "path to http api file loading"),
    FILE_UPLOAD(MultipartFile.class, "uploaded file");

    private String description;
    private Class supportedData;

    private DataSourceType(Class supportedData, String desc){
        this.supportedData = supportedData;
        this.description = desc;
    }

    public Class<?> getSupportedData(){
        return supportedData;
    }

    public String getDescription(){
        return description;
    }
}
