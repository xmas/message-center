package com.xmas.service.R;

import com.xmas.util.FileUtil;
import com.xmas.util.RandomNamesUtil;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Random;

@Service
public class RequestDirectoriesProcessor {

    public static final String INPUT_DIR_NAME = "/input";
    public static final String OUTPUT_DIR_NAME = "/output";

    private Random random = new Random();

    public File createDirectoriesForRequest(){
        String baseDirName = this.getClass().getResource("/R/data/").getPath() + getDirectoryName();

        FileUtil.createDirectory(baseDirName + INPUT_DIR_NAME);
        FileUtil.createDirectory(baseDirName + OUTPUT_DIR_NAME);

        File baseDir = new File(baseDirName);
        return baseDir;
    }

    protected String getDirectoryName(){
        return RandomNamesUtil.getRandomName();
    }

}
