package com.xmas.service.datasource;

import com.xmas.entity.Question;
import com.xmas.exceptions.ProcessingException;
import com.xmas.service.DataSource;
import com.xmas.service.QuestionHelper;
import com.xmas.util.data.FileSystemData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class DataService {

    @Autowired
    private QuestionHelper questionHelper;

    public LocalDateTime evaluateData(Question question, Object data) {
        return new FileSystemData(questionHelper.getQuestionDirFullPath(question))
                .evaluateData(getDataSource(question.getDataSourceType(), data).getData());
    }

    private DataSource getDataSource(DataSourceType dataSourceType, Object resource) {
        if (!checkDataType(dataSourceType, resource))
            throw new ProcessingException("Unsupported data for " + dataSourceType.getDescription());

        switch (dataSourceType) {
            case NONE: return new NoneDataSource();
            case HTTP_API: return new HTTPAPIDataSource((String) resource);
            case FILE_UPLOAD: return new FileUploadDataSource((MultipartFile) resource);
            default: throw new ProcessingException("Unsupported data source type");
        }
    }


    private boolean checkDataType(DataSourceType dataSourceType, Object data) {
        return !dataSourceType.requireData() || dataSourceType.getSupportedData().isAssignableFrom(data.getClass());
    }


    public void packageQuestionData(Question question) {
        new FileSystemData(questionHelper.getQuestionDirFullPath(question)).packageEvaluatedFiles();
    }
}
