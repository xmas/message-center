package com.xmas.service.questions.datasource;

import com.xmas.entity.questions.Question;
import com.xmas.exceptions.ProcessingException;
import com.xmas.service.questions.DataSource;
import com.xmas.service.questions.QuestionHelper;
import com.xmas.service.questions.data.FileQuestionData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DataService {

    public void evaluateData(Question question, Object data) {
        new FileQuestionData(QuestionHelper.getQuestionDirFullPath(question))
                .evaluateData(getDataSource(question.getDataSourceType(), data)
                        .getData());
    }

    private DataSource getDataSource(DataSourceType dataSourceType, Object resource) {
        if (!checkDataType(dataSourceType, resource))
            throw new ProcessingException("Unsupported data for " + dataSourceType.getDescription());

        switch (dataSourceType) {
            case HTTP_API: return new HTTPAPIDataSource((String) resource);
            case FILE_UPLOAD: return new FileUploadDataSource((MultipartFile) resource);
            default: throw new ProcessingException("Unsupported data source type");
        }
    }


    private boolean checkDataType(DataSourceType dataSourceType, Object data) {
        return dataSourceType.getSupportedData().isAssignableFrom(data.getClass());
    }


}
