package com.xmas.util.script;

import com.xmas.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;

public class ScriptFileUtil {

    public static final String SCRIPT_DIRECTORY_NAME = "script";
    public static final String SCRIPT_FILE_NAME = "script.sc";

    public static String getScript(String questionDir) {
        String scriptFilePath = Paths
                .get(questionDir, SCRIPT_DIRECTORY_NAME, SCRIPT_FILE_NAME)
                .toString();

        return new String(FileUtil.getFile(scriptFilePath));
    }

    public static void saveScript(String questionDir, MultipartFile scriptFile){
        File savedFile = new File(questionDir).toPath()
                .resolve(SCRIPT_DIRECTORY_NAME)
                .resolve(SCRIPT_FILE_NAME)
                .toFile();

        FileUtil.saveUploadedFile(savedFile, scriptFile);
        savedFile.setExecutable(true);
    }

    public static void replaceScript(String questionDir, MultipartFile scriptFile){
        File oldFile = new File(questionDir).toPath()
                .resolve(SCRIPT_DIRECTORY_NAME)
                .resolve(SCRIPT_FILE_NAME)
                .toFile();

        oldFile.deleteOnExit();

        saveScript(questionDir, scriptFile);
    }
}
