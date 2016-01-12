package com.xmas.service.questions.answer;

import com.xmas.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class AnswerTemplateUtil {

    public static final String TEMPLATE_DIR_NAME = "template";

    public static final String FILE_NAME = "template.json";

    public static void saveTemplate(String questionDir, MultipartFile file) {
        File dir = new File(questionDir).toPath().resolve(TEMPLATE_DIR_NAME).toFile();
        if (!dir.exists()) FileUtil.createDirectory(dir.getPath());

        FileUtil.saveUploadedFile(dir, FILE_NAME, file);
    }

    public static void replaceTemplate(String questionDir, MultipartFile newFile) {
        File oldFile = new File(questionDir).toPath().resolve(TEMPLATE_DIR_NAME).resolve(FILE_NAME).toFile();

        oldFile.deleteOnExit();

        saveTemplate(questionDir, newFile);
    }


}
