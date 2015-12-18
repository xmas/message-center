package com.xmas.notifiers.safari;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.exceptions.ZipCreationException;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ZipCreator {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private static final Logger logger = LogManager.getLogger(Zipper.class);

    @Autowired
    private WebsiteJsonEntity websiteJsonDefault;

    @Value("${safari.signature.password}")
    private String signaturePassword;

    public byte[] create(Long userID) {
        try {
            Zipper zip = new Zipper();
            zip.addFileToZip("icon.iconset", "icon_16x16.png", getResource(getFullPathFileName("/safari/icons/icon_16x16.png")), true);
            zip.addFileToZip("icon.iconset", "icon_16x16@2x.png", getResource(getFullPathFileName("/safari/icons/icon_16x16@2x.png")), true);
            zip.addFileToZip("icon.iconset", "icon_32x32.png", getResource(getFullPathFileName("/safari/icons/icon_32x32.png")), true);
            zip.addFileToZip("icon.iconset", "icon_32x32@2x.png", getResource(getFullPathFileName("/safari/icons/icon_32x32@2x.png")), true);
            zip.addFileToZip("icon.iconset", "icon_128x128.png", getResource(getFullPathFileName("/safari/icons/icon_128x128.png")), true);
            zip.addFileToZip("icon.iconset", "icon_128x128@2x.png", getResource(getFullPathFileName("/safari/icons/icon_128x128@2x.png")), true);

            zip.addFileToZip("", "website.json", getWebsiteJson(userID), true);
            byte[] manifest = zip.getManifest();
            zip.addFileToZip("", "manifest.json", manifest, false);
            PICS7Encrypt encrypt = new PICS7Encrypt(getResource(getFullPathFileName("/safari/ca.p12")), signaturePassword);
            zip.addFileToZip("", "signature", encrypt.sign(manifest), true);
            return zip.finalizeZip();
        } catch (Exception e) {
            throw new ZipCreationException(e);
        }
    }

    protected byte[] getResource(String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(fileName));
    }

    protected String getFullPathFileName(String fileName) throws IOException {
        URL resource = this.getClass().getClassLoader().getResource(fileName);
        if (resource != null) {
            return resource.getPath();
        } else {
            throw new IOException("Cant find resource with name " + fileName);
        }
    }

    protected byte[] getWebsiteJson(Long userId) throws JsonProcessingException {
        WebsiteJsonEntity websiteJsonEntity = websiteJsonDefault.clone();
        websiteJsonEntity.setAuthenticationToken(createAuthenticationToken(userId));
        return MAPPER.writeValueAsString(websiteJsonEntity).getBytes();
    }

    //Token must have at least 16 characters
    protected String createAuthenticationToken(Long userId){
        String rawToken = appendLength(userId.toString());
        return new String(Base64.encodeBase64(rawToken.getBytes()));
    }

    private String appendLength(String s){
        for (int i = 0; i <16-s.length(); i++) {
            s = "0" + s;
        }
        return s;
    }

    public Long encodeUserGUID(String token){
        return Long.parseLong(new String(Base64.decodeBase64(token.getBytes())));
    }
}
