package com.xmas.service.datasource;

import org.springframework.web.multipart.MultipartFile;

public enum DataSourceType {
    NONE(Object.class, "nothing", true, false),
    HTTP_API(String.class, "path to http api file loading", true, true),
    FILE_UPLOAD(MultipartFile.class, "uploaded file", false, true);

    private String description;
    private Class supportedData;
    private boolean supportScheduling;
    private boolean requireData;

    DataSourceType(Class supportedData, String desc, boolean supportScheduling, boolean requireData){
        this.supportedData = supportedData;
        this.description = desc;
        this.supportScheduling = supportScheduling;
        this.requireData = requireData;
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

    public boolean requireData(){
        return requireData;
    }
}
