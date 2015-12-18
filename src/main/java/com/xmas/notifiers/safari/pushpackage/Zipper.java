package com.xmas.notifiers.safari.pushpackage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zipper {

    private static final Logger logger = LogManager.getLogger(Zipper.class);

    private final ByteArrayOutputStream bos;
    private final ZipOutputStream zipFile;
    private final Map<String, String> manifest;

    public Zipper() {
        bos = new ByteArrayOutputStream();
        zipFile = new ZipOutputStream(bos);
        manifest = new HashMap<>();
    }

    public byte[] getManifest() throws UnsupportedEncodingException {
        Set<String> keys = manifest.keySet();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            sb.append(i <= 0 ? "{" : ",");
            sb.append("\"").append(key).append("\": \"").append(manifest.get(key)).append("\"");
            i++;
        }
        sb.append("}");
        return sb.toString().getBytes("UTF8");
    }

    public byte[] finalizeZip() throws IOException {
        zipFile.finish();
        bos.flush();
        zipFile.close();
        manifest.clear();
        return bos.toByteArray();
    }

    public void addFileToZip(String path, String filename, byte[] file, boolean addToManifest) throws IOException {
        if (file != null) {
            String completeFilename = path.length() > 0 ? path + "/" + filename : filename;
            ZipEntry zipEntry = new ZipEntry(completeFilename);
            CRC32 crc = new CRC32();
            crc.update(file);
            zipEntry.setCrc(crc.getValue());
            zipFile.putNextEntry(zipEntry);
            zipFile.write(file, 0, file.length);
            zipFile.flush();
            zipFile.closeEntry();

            if (addToManifest) {
                try {
                    manifest.put(completeFilename, SHASum(file));
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            logger.error("File " + filename + " was null!");
        }
    }

    public static String SHASum(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return byteArray2Hex(md.digest(data));
    }

    private static String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String formattedString = formatter.toString();
        formatter.close();
        return formattedString;

    }

}

