package com.xmas.service.questions.data;

import com.xmas.exceptions.ProcessingException;
import com.xmas.util.FileUtil;
import com.xmas.util.RandomNamesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileQuestionData {

    private static final Logger logger = LogManager.getLogger();

    private String dataDirPath;

    public void evaluateData(byte[] data) {
        prepareDirectory();
        saveFile(data);
    }

    private void saveFile(byte[] data) {
        String filePath = dataDirPath + "/" + RandomNamesUtil.getRandomName() + ".dat";
        File file = new File(filePath);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(data);
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.debug(e.getMessage(), e);
            throw new ProcessingException("Cant store file: " + filePath);
        }
    }

    private void prepareDirectory() {
        File questionDir = new File(dataDirPath);
        if (!questionDir.exists()) {
            FileUtil.createDirectory(dataDirPath);
        } else {
            packagePreviousFiles(questionDir);
        }
    }

    private void packagePreviousFiles(File directory) {
        List<File> files = Stream.of(directory.listFiles())
                .filter(File::isFile)
                .collect(Collectors.toList());

        if(!files.isEmpty()){
            moveFiles(files, createDailyPackageDir(directory));
        }
    }

    private File createDailyPackageDir(File parentDir) {
        String dailyDirName = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("ddMMyyyy"));

        File dailyDr = parentDir.toPath().resolve(dailyDirName).toFile();

        if (dailyDr.mkdir())
            return dailyDr;
        else
            throw new ProcessingException("Can't create directory for old question data");
    }

    private void moveFiles(List<File> files, File destinationDirectory){
        files.stream().forEach(file -> {
            try {
                Files.move( file.toPath(),
                            destinationDirectory
                                    .toPath()
                                    .resolve(file.toPath().getFileName().toString()));
            } catch (IOException e) {
                throw new ProcessingException("Can't move old files");
            }
        });
    }

    public String getDataDirPath() {
        return dataDirPath;
    }

    public void setDataDirPath(String dataDirPath) {
        this.dataDirPath = dataDirPath;
    }
}
