package com.xmas.service.R;

import com.xmas.exceptions.R.DirCreationException;
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
        File baseDir = new File(baseDirName);
        if(!baseDir.mkdir()) throw new DirCreationException(baseDirName);
        File input = new File(baseDirName + INPUT_DIR_NAME);
        if(!input.mkdir()) throw new DirCreationException(baseDirName + INPUT_DIR_NAME);
        File output = new File(baseDirName + OUTPUT_DIR_NAME);
        if(!output.mkdir()) throw new DirCreationException(baseDirName + OUTPUT_DIR_NAME);
        return baseDir;
    }

    protected String getDirectoryName(){
        return RandomNamesUtil.getRandomName();
    }

}
