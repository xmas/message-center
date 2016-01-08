package com.xmas.service.questions.data;

import com.xmas.exceptions.ProcessingException;
import com.xmas.service.questions.datasource.DataSource;
import com.xmas.util.RandomNamesUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class FileQuestionData implements QuestionData {

    private static final Logger logger = LogManager.getLogger();

    private String dataDirPath;

    private String filePath;

    private DataSource dataSource;

    @Override
    public DataType getType() {
        return DataType.FILE;
    }

    @Override
    public void evaluateDailyData() {
        saveFile();
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private void saveFile() {
        filePath = dataDirPath + "/" + RandomNamesUtil.getRandomName() + getFileExtension();
        File file = new File(filePath);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(dataSource.getData());
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.debug(e.getMessage(), e);
            throw new ProcessingException("Cant store file: " + filePath);
        }
    }

    public String getDataDirPath() {
        return dataDirPath;
    }

    public void setDataDirPath(String dataDirPath) {
        this.dataDirPath = dataDirPath;
    }

    public abstract String getFileExtension();
}
