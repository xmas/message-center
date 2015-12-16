package com.xmas.notifiers.safari;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.exceptions.ZipCreationException;
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
    WebsiteJsonEntity websiteJson;

    @Value("safari.signature.password")
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

            zip.addFileToZip("", "website.json", getWebsiteJson(), true);
            byte[] manifest = zip.getManifest();
            zip.addFileToZip("", "manifest.json", manifest, false);
            PICS7Encrypt encrypt = new PICS7Encrypt(getResource("/safari/ca.p12"), signaturePassword);
            zip.addFileToZip("", "signature", encrypt.sign(manifest), true);
            return zip.finalizeZip();
        } catch (Exception e) {
            throw new ZipCreationException(e);
        }
    }

    private byte[] getResource(String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(fileName));
    }

    private String getFullPathFileName(String fileName) throws IOException {
        URL resource = this.getClass().getClassLoader().getResource(fileName);
        if (resource != null) {
            return resource.getPath();
        } else {
            throw new IOException("Cant find resource with name " + fileName);
        }
    }

    private byte[] getWebsiteJson(Long userId) throws JsonProcessingException {

        return MAPPER.writeValueAsString(websiteJson).getBytes();
    }

    private String createAutenticationToken(Long userId){
        
    }
}
