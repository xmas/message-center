package com.xmas.service.questions.answer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.entity.questions.Answer;
import com.xmas.exceptions.BadRequestException;
import com.xmas.exceptions.ProcessingException;
import com.xmas.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.stream.Stream;

public class AnswerTemplateUtil {

    public static final String TEMPLATE_DIR_NAME = "template";
    public static final String FILE_NAME = "template.json";

    public static final String[] REQUIRED_FIELDS = {"title", "details"};

    /**
     * Save uploaded file into questionDir/template/template.json
     *
     * @param questionDir base dir for question
     * @param file        uploaded file
     */
    public static void saveTemplate(String questionDir, MultipartFile file) {
        checkAnswerTemplate(file);

        File dir = new File(questionDir).toPath().resolve(TEMPLATE_DIR_NAME).toFile();
        if (!dir.exists()) FileUtil.createDirectory(dir.getPath());

        FileUtil.saveUploadedFile(dir, FILE_NAME, file);
    }

    /**
     * Replace old template file with uploaded new
     * If there wasn't old file just save new
     *
     * @param questionDir base dir for question
     * @param newFile     uploaded file
     */
    public static void replaceTemplate(String questionDir, MultipartFile newFile) {
        checkAnswerTemplate(newFile);

        File oldFile = new File(questionDir).toPath().resolve(TEMPLATE_DIR_NAME).resolve(FILE_NAME).toFile();

        oldFile.deleteOnExit();

        saveTemplate(questionDir, newFile);
    }

    /**
     * Check uploaded file if it has correct structure
     *
     * @param file uploaded file
     * @throws com.xmas.exceptions.BadRequestException if it is incorrect
     */
    private static void checkAnswerTemplate(MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Answer template = mapper.reader().forType(Answer.class).readValue(file.getBytes());

            checkRequiredFields(template);

        } catch (JsonProcessingException jpe) {
            throw new BadRequestException("Bad json structure uploaded", jpe);
        } catch (IOException e) {
            throw new ProcessingException("Can't read from uploaded file", e);
        }
    }

    private static boolean checkRequiredFields(Answer template) {
        Stream.of(REQUIRED_FIELDS).forEach(field -> checkFieldExist(template, field));
        return true;
    }

    private static boolean checkFieldExist(Answer template, String fieldName) {
        try {
            Field field = Answer.class.getDeclaredField(fieldName);
            field.setAccessible(true);

            String value = (String) field.get(template);
            if (value == null || value.isEmpty())
                throw new BadRequestException("Answer template do not have required field " + fieldName + " or it is empty.");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new BadRequestException("Answer template do not have required field " + fieldName + " or it is empty.");
        }
        return true;
    }


}
