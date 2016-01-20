package com.xmas.service.questions.datasource;

import com.xmas.exceptions.ProcessingException;
import com.xmas.service.questions.DataSource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileUploadDataSource implements DataSource{

    private MultipartFile sourceFile;

    public FileUploadDataSource(MultipartFile input){
        sourceFile = input;
    }

    @Override
    public byte[] getData() {
        try {
            return sourceFile.getBytes();
        } catch (IOException e) {
            throw new ProcessingException("Can't get data from uploaded file");
        }
    }
}
