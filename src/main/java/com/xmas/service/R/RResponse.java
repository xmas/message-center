package com.xmas.service.R;

import java.util.List;

public class RResponse {
    private String directory;
    private List<String> files;

    public RResponse(){

    }

    public RResponse(String directory, List<String> files) {
        this.directory = directory;
        this.files = files;
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
