package com.xmas.location.provider;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Optional;

@Service
public class IPLocationProvider {

    private static final String MODE = "ip-city";
    private static final String FORMAT = "json";

    private static final HttpClient HTTP_CLIENT = new DefaultHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Logger logger = LogManager.getLogger(IPLocationProvider.class);

    @Value("ipinfodb.api.key")
    private String apiKey;
    @Value("ipinfodb.api.url")
    private String apiUrl;

    static {
        // Add a handler to handle unknown properties (in case the API adds new properties to the response)
        MAPPER.addHandler(new DeserializationProblemHandler() {
            @Override
            public boolean handleUnknownProperty(DeserializationContext context, JsonParser jp, JsonDeserializer<?> deserializer, Object beanOrClass, String propertyName) throws IOException {
                // Do not fail - just log
                String className = (beanOrClass instanceof Class) ? ((Class) beanOrClass).getName() : beanOrClass.getClass().getName();
                System.out.println("Unknown property while de-serializing: " + className + "." + propertyName);
                context.getParser().skipChildren();
                return true;
            }
        });
    }

    public Optional<String> getLocation(String ip){
        if(true)
            return Optional.of("LOCATION");

        String url = apiUrl + MODE + "/?format=" + FORMAT + "&key=" + apiKey + "&ip=" + ip;

        try {
            HttpGet request = new HttpGet(url);
            HttpResponse response = HTTP_CLIENT.execute(request, new BasicHttpContext());
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                logger.error("Cant get location info from " + apiUrl + " with key " + apiKey + "for IP " + ip +
                        ". IpInfoDb response is " + response.getStatusLine());
                return Optional.empty();
            }

            String responseBody = EntityUtils.toString(response.getEntity());
            IPLocationResponseMessageEntity ipCityResponse = MAPPER.readValue(responseBody, IPLocationResponseMessageEntity.class);
            if ("OK".equals(ipCityResponse.getStatusCode())) {
                String result = ipCityResponse.getCountryCode() + ", " + ipCityResponse.getRegionName() + ", " + ipCityResponse.getCityName();
                return Optional.of(result);
            } else {
                logger.error("Cant get location info from " + apiUrl + " with key " + apiKey + "for IP " + ip +
                        ". API status message is '" + ipCityResponse.getStatusMessage() + "'");
                return Optional.empty();
            }
        } catch (IOException e) {
            logger.error("Cant get location info from " + apiUrl + " with key " + apiKey + "for IP " + ip +
                    ". Error message is '" + e.getMessage() + "'");
            logger.debug(e.getMessage(), e);
            return Optional.empty();
        }

    }

    @PreDestroy
    public void destroy(){
        HTTP_CLIENT.getConnectionManager().shutdown();
    }



}
