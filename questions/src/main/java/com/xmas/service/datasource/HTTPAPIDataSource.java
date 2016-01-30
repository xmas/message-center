package com.xmas.service.datasource;

import com.xmas.exceptions.ProcessingException;
import com.xmas.service.DataSource;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class HTTPAPIDataSource implements DataSource {

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
        } catch (IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            logger.error(e.getMessage());
            logger.debug(e.getMessage(), e);
            throw new ProcessingException(EXCEPTION_MESSAGE + apiPath);
        }
    }

    private byte[] loadData() throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        HttpEntity responseEntity = requestData().getEntity();
        return readContent(responseEntity.getContent());
    }

    private byte[] readContent(InputStream stream) throws IOException {
        return IOUtils.toByteArray(stream);
    }

    private HttpResponse requestData() throws IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        HttpClient client = HttpClientBuilder.create().setSslcontext(new SSLContextBuilder().loadTrustMaterial(null, (c, s) -> true).build())
                .build();

        HttpResponse res = client
                .execute(new HttpGet(apiPath));
        if(res != null)
            return res;
        else throw new ProcessingException(EXCEPTION_MESSAGE + apiPath);
    }
}
