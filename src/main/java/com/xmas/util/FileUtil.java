package com.xmas.util;

import com.xmas.exceptions.ResourceNotFoundException;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    public static byte[] getResource(String fileName) {
        try {
            return Files.readAllBytes(Paths.get(getFullPathFileName(fileName)));
        }catch (IOException e){
            throw new ResourceNotFoundException(e);
        }

    }

    public static String getFullPathFileName(String fileName) {
        URL resource = FileUtil.class.getClassLoader().getResource(fileName);
        if (resource != null) {
            return resource.getPath();
        } else {
            throw new ResourceNotFoundException("Cant find resource with name " + fileName);
        }
    }
}
