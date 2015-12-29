package com.xmas.R.service;

import com.xmas.exceptions.DirCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class RequestDirectoriesProcessor {

    public static final String INPUT_DIR_NAME = "/input";
    public static final String OUTPUT_DIR_NAME = "/output";

    @Value("${r.files.directory}")
    private String rFilesDirectory;

    private Random random = new Random();

    public File createDirectoriesForRequest(){
        String baseDirName = rFilesDirectory + "/" + getDirectoryName();
        File baseDir = new File(baseDirName);
        if(!baseDir.mkdir()) throw new DirCreationException(baseDirName);
        File input = new File(baseDirName + INPUT_DIR_NAME);
        if(!input.mkdir()) throw new DirCreationException(baseDirName + INPUT_DIR_NAME);
        File output = new File(baseDirName + OUTPUT_DIR_NAME);
        if(!output.mkdir()) throw new DirCreationException(baseDirName + OUTPUT_DIR_NAME);
        return baseDir;
    }

    protected String getDirectoryName(){
        LocalDateTime dateTime = LocalDateTime.now();
        String timePart = dateTime.format(DateTimeFormatter.ofPattern("YYMMddhhmmss"));
        Integer randomPart = random.nextInt(10000000);
        return toHex(timePart + randomPart);
    }

    protected String toHex(String digits){
        return Long.toHexString(Long.valueOf(digits));
    }

}
