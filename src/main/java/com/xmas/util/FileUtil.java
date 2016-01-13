package com.xmas.util;

import com.xmas.exceptions.NotFoundException;
import com.xmas.exceptions.ProcessingException;
import com.xmas.exceptions.push.ResourceNotFoundException;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

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
        return getFile(getFullPathFileName(fileName));
    }

    public static byte[] getFile(String fileName){
        try {
            return Files.readAllBytes(Paths.get(fileName));
        } catch (IOException e) {
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

    public static List<String> getFiles(String folder) {
        File dir = new File(folder);
        if (!dir.exists()) throw new NotFoundException("Resource nor found.");
        return Arrays.stream(dir.list()).collect(Collectors.toList());
    }

    public static void saveUploadedFile(File destDir, String fileName, MultipartFile fileToSave) {
        File file = destDir.toPath().resolve(fileName).toFile();
        saveUploadedFile(file, fileToSave);
    }

    public static void saveUploadedFile(File destFile, MultipartFile fileToSave) {
        try {
            FileUtils.writeByteArrayToFile(destFile, fileToSave.getBytes());
        } catch (IOException e) {
            throw new ProcessingException("Cant store file: " + destFile);
        }
    }

    public static File createDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new ProcessingException("Can't create directory " + dirPath);
            }
        }
        return dir;
    }
}
