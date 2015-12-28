package com.xmas.R.service;

import java.util.List;

public class RResponse {
    private String error;
    private String directory;
    private List<String> files;

    public RResponse(){

    }

    public RResponse(String directory, List<String> files) {
        this.directory = directory;
        this.files = files;
    }

    public RResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }
}
