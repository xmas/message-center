package com.xmas.service.questions.data;

import com.xmas.exceptions.ProcessingException;
import com.xmas.util.FileUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.*;

public class FileSystemQuestionData {

    private static final Logger logger = LogManager.getLogger();

    public static final String INPUT_FILE_NAME = "input.dat";
    public static final String INFO_FILE_NAME = ".info";

    private String dataDirPath;

    public FileSystemQuestionData(String dataDirPath) {
        this.dataDirPath = dataDirPath;
    }

    public LocalDateTime evaluateData(byte[] data) {
        LocalDateTime evaluationTime = prepareDirectory();
        if(data != null)
            saveFile(data);
        return evaluationTime;
    }

    private void saveFile(byte[] data) {
        String filePath = dataDirPath + "/" + INPUT_FILE_NAME;
        File file = new File(filePath);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(data);
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.debug(e.getMessage(), e);
            throw new ProcessingException("Cant store file: " + filePath);
        }
    }

    private LocalDateTime prepareDirectory() {
        File questionDir = new File(dataDirPath);
        if (!questionDir.exists()) {
            FileUtil.createDirectory(dataDirPath);
        } else {
            packagePreviousFiles(questionDir);
        }
        return addInfoFile(dataDirPath);
    }

    private void packagePreviousFiles(File directory) {
        File[] filesInDir = directory.listFiles();

        List<File> files = Stream.of(filesInDir == null ? new File[]{} : filesInDir)
                .filter(File::isFile)
                .collect(Collectors.toList());

        if (!files.isEmpty()) {
            moveFiles(files, createDailyPackageDir(directory));
        }
    }

    private File createDailyPackageDir(File parentDir) {
        String dailyDirName = getCreatedDateTime(parentDir.getAbsolutePath());

        File dailyDr = parentDir.toPath().resolve(dailyDirName).toFile();

        if (dailyDr.mkdir())
            return dailyDr;
        else if (dailyDr.exists())
            return dailyDr;
        else
            throw new ProcessingException("Can't create directory for old question data");
    }

    private void moveFiles(List<File> files, File destinationDirectory) {
        files.stream()
                .filter(file -> !file.toPath().getFileName().toString().equals(INFO_FILE_NAME))
                .forEach(file -> moveFile(file, destinationDirectory));
    }

    private void moveFile(File file, File destinationDirectory){
        try {
            Files.move(file.toPath(),
                    destinationDirectory
                            .toPath()
                            .resolve(file.toPath().getFileName().toString()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ProcessingException("Can't move old files");
        }
    }

    private LocalDateTime addInfoFile(String questionDir){
        Path infoFile = new File(questionDir).toPath().resolve(INFO_FILE_NAME);
        try {
            LocalDateTime now = LocalDateTime.now();
            String data = now.toString();
            Files.write(infoFile, data.getBytes(), WRITE, TRUNCATE_EXISTING, CREATE);
            return now;
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.debug(e.getMessage(), e);
            throw new ProcessingException("Can't write to info file " + infoFile);
        }
    }

    private String getCreatedDateTime(String questionDir){
        Path infoFile = new File(questionDir).toPath().resolve(INFO_FILE_NAME);
        try {
            return new String(Files.readAllBytes(infoFile));
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.debug(e.getMessage(), e);
            throw new ProcessingException("Can't read from info file " + infoFile);
        }
    }
}