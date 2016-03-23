package com.xmas.service.notifiers.safari.pushpackage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xmas.exceptions.ZipCreationException;
import com.xmas.service.notifiers.safari.WebsiteJsonEntity;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.operator.OperatorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static com.xmas.util.FileUtil.getResource;

@Service
public class ZipCreator {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Autowired
    private WebsiteJsonEntity websiteJsonDefault;

    @Value("${safari.signature.password}")
    private String signaturePassword;

    public byte[] create(Long userID) {
        try {
            Zipper zip = new Zipper();
            addIcons(zip);
            addJsonFiles(zip, userID);
            addSignature(zip);
            return zip.finalizeZip();
        } catch (Exception e) {
            throw new ZipCreationException(e);
        }
    }

    private void addIcons(Zipper zip) throws IOException {
        zip.addFileToZip("icon.iconset", "icon_16x16.png", getResource("/safari/icons/icon_16x16.png"), true);
        zip.addFileToZip("icon.iconset", "icon_16x16@2x.png", getResource("/safari/icons/icon_16x16@2x.png"), true);
        zip.addFileToZip("icon.iconset", "icon_32x32.png", getResource("/safari/icons/icon_32x32.png"), true);
        zip.addFileToZip("icon.iconset", "icon_32x32@2x.png", getResource("/safari/icons/icon_32x32@2x.png"), true);
        zip.addFileToZip("icon.iconset", "icon_128x128.png", getResource("/safari/icons/icon_128x128.png"), true);
        zip.addFileToZip("icon.iconset", "icon_128x128@2x.png", getResource("/safari/icons/icon_128x128@2x.png"), true);
    }

    private void addJsonFiles(Zipper zip, Long userID) throws IOException {
        zip.addFileToZip("", "website.json", getWebsiteJson(userID), true);
        zip.addFileToZip("", "manifest.json", zip.getManifest(), false);
    }

    private void addSignature(Zipper zip) throws IOException, OperatorCreationException, GeneralSecurityException, CMSException {
        PICS7Encrypt encrypt = new PICS7Encrypt(getResource("/safari/ca.p12"), signaturePassword);
        zip.addFileToZip("", "signature", encrypt.sign(zip.getManifest()), false);
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
