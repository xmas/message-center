package com.xmas.notifiers.safari.pushpackage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.exceptions.ZipCreationException;
import com.xmas.notifiers.safari.WebsiteJsonEntity;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.xmas.util.FileUtil.getResource;

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
            zip.addFileToZip("icon.iconset", "icon_16x16.png", getResource("/safari/icons/icon_16x16.png"), true);
            zip.addFileToZip("icon.iconset", "icon_16x16@2x.png", getResource("/safari/icons/icon_16x16@2x.png"), true);
            zip.addFileToZip("icon.iconset", "icon_32x32.png", getResource("/safari/icons/icon_32x32.png"), true);
            zip.addFileToZip("icon.iconset", "icon_32x32@2x.png", getResource("/safari/icons/icon_32x32@2x.png"), true);
            zip.addFileToZip("icon.iconset", "icon_128x128.png", getResource("/safari/icons/icon_128x128.png"), true);
            zip.addFileToZip("icon.iconset", "icon_128x128@2x.png", getResource("/safari/icons/icon_128x128@2x.png"), true);
            zip.addFileToZip("", "website.json", getWebsiteJson(userID), true);
            zip.addFileToZip("", "manifest.json", zip.getManifest(), false);

            PICS7Encrypt encrypt = new PICS7Encrypt(getResource("/safari/ca.p12"), signaturePassword);
            zip.addFileToZip("", "signature", encrypt.sign(zip.getManifest()), false);

            return zip.finalizeZip();

        } catch (Exception e) {
            throw new ZipCreationException(e);
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
        return new String(Base64.encodeBase64(rawToken.getBytes())).replace("==", "");
    }

    private String appendLength(String s){
        String res = s;
        for (int i = 0; i < 16-s.length(); i++) {
            res = "0" + res;
        }
        return res;
    }

    public Long encodeUserGUID(String token){
        return Long.parseLong(new String(Base64.decodeBase64((token+"==").getBytes())));
    }
}
