package com.xmas.service.questions.datasource;

import com.xmas.exceptions.ProcessingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class HTTPAPIDataSource implements DataSource{

    private static final String EXCEPTION_MESSAGE = "Cant read response data from API: ";

    private static final Logger logger = LogManager.getLogger();

    private String apiPath;

    public HTTPAPIDataSource(String apiPath) {
        this.apiPath = apiPath;
    }

    @Override
    public byte[] getData() {
        try {
            return loadData();
        } catch (IOException e) {
            logger.error(e.getMessage());
            logger.debug(e.getMessage(), e);
            throw new ProcessingException(EXCEPTION_MESSAGE + apiPath);
        }
    }

    private byte[] loadData() throws IOException {
        HttpEntity responseEntity = requestData().getEntity();
        int dataLength = (int) responseEntity.getContentLength();
        byte[] result = new byte[dataLength];

        if(responseEntity.getContent().read(result, 0, dataLength) != dataLength)
            throw new ProcessingException(EXCEPTION_MESSAGE + apiPath);

        return result;
    }

    private HttpResponse requestData() throws IOException {
        HttpResponse res = new DefaultHttpClient().execute(new HttpGet(apiPath));
        if(res != null)
            return res;
        else throw new ProcessingException(EXCEPTION_MESSAGE + apiPath);
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }
}
