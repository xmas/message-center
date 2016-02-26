package com.xmas.util.script;

import com.xmas.exceptions.BadRequestException;
import com.xmas.exceptions.ProcessingException;
import com.xmas.util.FileUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ScriptFileUtil {

    private static final Map<ScriptType, String> scryptHeaders = new HashMap<ScriptType, String>() {{
        put(ScriptType.NODE, "#!/usr/bin/nodejs");
    }};

    public static final String SCRIPT_DIRECTORY_NAME = "script";
    public static final String SCRIPT_FILE_NAME = "script.sc";

    public static String getScript(String workDir) {
        String scriptFilePath = Paths
                .get(workDir, SCRIPT_DIRECTORY_NAME, SCRIPT_FILE_NAME)
                .toString();

        return new String(FileUtil.getFile(scriptFilePath));
    }

    public static void replaceScript(String workDir, MultipartFile scriptFile, ScriptType type) {
        File oldFile = new File(workDir).toPath()
                .resolve(SCRIPT_DIRECTORY_NAME)
                .resolve(SCRIPT_FILE_NAME)
                .toFile();

        oldFile.deleteOnExit();

        saveScript(workDir, scriptFile, type);
    }

    public static void saveScript(String workDir, MultipartFile scriptFile, ScriptType type) {
        if(isScriptEmpty(scriptFile)) throw new BadRequestException("Uploaded script file is empty.");

        File savedFile = new File(workDir).toPath()
                .resolve(SCRIPT_DIRECTORY_NAME)
                .resolve(SCRIPT_FILE_NAME)
                .toFile();

        FileUtil.saveUploadedFile(savedFile, checkAndReplaceHeader(scriptFile, type));
        if (!savedFile.setExecutable(true)) {
            throw new ProcessingException("Can't set script file as executable. Maybe not enough permissions.");
        }
    }

    private static boolean isScriptEmpty(MultipartFile file){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream(file)))) {
            return ! reader
                    .lines()
                    .filter(s -> ! s.isEmpty())
                    .findAny()
                    .isPresent();
        } catch (IOException e) {
            throw new ProcessingException("Can't save uploaded script file.", e);
        }
    }

    private static InputStream checkAndReplaceHeader(MultipartFile file, ScriptType type){
        if(scryptHeaders.containsKey(type)){
            final String actualHeader = getHeader(file).trim();
            final String rightHeader = scryptHeaders.get(type);

            if(actualHeader.isEmpty()){
                return addHeader(file, rightHeader);
            } else if(! actualHeader.equals(rightHeader)){
                return replaceHeader(file, rightHeader);
            }
        }
        return getInputStream(file);
    }

    private static InputStream getInputStream(MultipartFile file){
        try {
            return file.getInputStream();
        } catch (IOException e) {
            throw new ProcessingException("Can't save uploaded script file.", e);
        }
    }

    private static InputStream replaceHeader(MultipartFile file, String rightHeader){
        return replaceLinesInFile(file, rightHeader, 1);
    }

    private static InputStream addHeader(MultipartFile file, String header){
        return replaceLinesInFile(file, header, 0);
    }

    private static InputStream replaceLinesInFile(MultipartFile file, String newLine, long linesCount){
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream(file)))
        ) {
            writer.write(newLine);
            reader.lines().skip(linesCount).forEach(line -> writeLine(line, writer));
            writer.flush();
            return new ByteArrayInputStream(outputStream.toByteArray());
        } catch (IOException e) {
            throw new ProcessingException("Can't save uploaded script file.", e);
        }
    }

    private static void writeLine(String line, BufferedWriter writer){
        try {
            writer.newLine();
            writer.write(line);
        } catch (IOException e) {
            throw new ProcessingException("Can't save uploaded script file.", e);
        }
    }

    private static String getHeader(MultipartFile multipartFile){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream(multipartFile)))) {
            return reader
                    .lines()
                    .limit(1)
                    .findFirst()
                    .filter(ScriptFileUtil::isHeader)
                    .orElse("");
        } catch (IOException e) {
            throw new ProcessingException("Can't save uploaded script file.", e);
        }
    }

    private static boolean isHeader(String line){
        return line.matches("^#!.*$");
    }
}
