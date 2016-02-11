package com.xmas.util.script;

import com.xmas.exceptions.ProcessingException;
import com.xmas.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;

public class ScriptFileUtil {

    public static final String SCRIPT_DIRECTORY_NAME = "script";
    public static final String SCRIPT_FILE_NAME = "script.sc";

    public static String getScript(String workDir) {
        String scriptFilePath = Paths
                .get(workDir, SCRIPT_DIRECTORY_NAME, SCRIPT_FILE_NAME)
                .toString();

        return new String(FileUtil.getFile(scriptFilePath));
    }

    public static void saveScript(String workDir, MultipartFile scriptFile){
        File savedFile = new File(workDir).toPath()
                .resolve(SCRIPT_DIRECTORY_NAME)
                .resolve(SCRIPT_FILE_NAME)
                .toFile();

        FileUtil.saveUploadedFile(savedFile, scriptFile);
        if(! savedFile.setExecutable(true)){
            throw new ProcessingException("Can't set script file as executable. Maybe not enough permissions.");
        }
    }

    public static void replaceScript(String workDir, MultipartFile scriptFile){
        File oldFile = new File(workDir).toPath()
                .resolve(SCRIPT_DIRECTORY_NAME)
                .resolve(SCRIPT_FILE_NAME)
                .toFile();

        oldFile.deleteOnExit();

        saveScript(workDir, scriptFile);
    }
}
