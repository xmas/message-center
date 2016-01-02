package com.xmas.util;

import com.xmas.exceptions.NotFoundException;
import com.xmas.exceptions.ResourceNotFoundException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<String> getFiles(String folder){
        File dir = new File(folder);
        if(!dir.exists()) throw new NotFoundException("Resource nor found.");
        return Arrays.stream(dir.list()).collect(Collectors.toList());
    }
}
