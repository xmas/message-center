package com.xmas.notifiers.safari;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("unused")
public class WebsiteJsonEntity implements Cloneable{
    @Value("${safari.website.json.websiteName}")
    private String websiteName;
    @Value("${safari.website.json.websitePushID}")
    private String websitePushID;
    @Value("${safari.website.json.allowedDomains}")
    private String allowedDomains;
    @Value("${safari.website.json.urlFormatString}")
    private String urlFormatString;
    @Value("${safari.website.json.authenticationToken}")
    private String authenticationToken;
    @Value("${safari.website.json.webServiceURL}")
    private String webServiceURL;

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getWebsitePushID() {
        return websitePushID;
    }

    public void setWebsitePushID(String websitePushID) {
        this.websitePushID = websitePushID;
    }

    public String getAllowedDomains() {
        return allowedDomains;
    }

    public void setAllowedDomains(String allowedDomains) {
        this.allowedDomains = allowedDomains;
    }

    public String getUrlFormatString() {
        return urlFormatString;
    }

    public void setUrlFormatString(String urlFormatString) {
        this.urlFormatString = urlFormatString;
    }

    public String getAuthenticationToken() {
        return authenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        this.authenticationToken = authenticationToken;
    }

    public String getWebServiceURL() {
        return webServiceURL;
    }

    public void setWebServiceURL(String webServiceURL) {
        this.webServiceURL = webServiceURL;
    }

    @Override
    public WebsiteJsonEntity clone(){
        WebsiteJsonEntity clone = new WebsiteJsonEntity();
        clone.urlFormatString = urlFormatString;
        clone.allowedDomains = allowedDomains;
        clone.webServiceURL = webServiceURL;
        clone.websiteName = websiteName;
        clone.websitePushID = websitePushID;
        return clone;
    }
}
