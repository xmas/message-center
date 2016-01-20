package com.xmas.service.questions.datasource;

import org.springframework.web.multipart.MultipartFile;

public enum DataSourceType {
    HTTP_API(String.class, "path to http api file loading", true),
    FILE_UPLOAD(MultipartFile.class, "uploaded file", false);

    private String description;
    private Class supportedData;
    private boolean supportScheduling;

    DataSourceType(Class supportedData, String desc, boolean supportScheduling){
        this.supportedData = supportedData;
        this.description = desc;
        this.supportScheduling = supportScheduling;
    }

    public Class<?> getSupportedData(){
        return supportedData;
    }

    public String getDescription(){
        return description;
    }

    public boolean supportScheduling(){
        return supportScheduling;
    }
}
