package com.xmas.notifiers.safari.pushpackage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.*;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PICS7Encrypt {

    private static final Logger logger = LogManager.getLogger(Zipper.class);

    private final byte[] certificate;
    private final String password;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public PICS7Encrypt(byte[] store, String storepass) {
        assert store != null;
        certificate = store;
        password = storepass;
    }

    private KeyStore getKeystore() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");
        clientStore.load(new ByteArrayInputStream(certificate), password.toCharArray());
        return clientStore;
    }

    private X509CertificateHolder getCert(KeyStore keystore, String aliaz) throws GeneralSecurityException, IOException {
        java.security.cert.Certificate c = keystore.getCertificate(aliaz);
        return new X509CertificateHolder(c.getEncoded());
    }

    private PrivateKey getPrivateKey(KeyStore keystore, String aliaz) throws GeneralSecurityException, IOException {
        return (PrivateKey) keystore.getKey(aliaz, password.toCharArray());
    }

    public byte[] sign(byte[] dataToSign) throws IOException, GeneralSecurityException, OperatorCreationException, CMSException {
        KeyStore clientStore = getKeystore();
        if (clientStore == null) {
            return null;
        }
        Enumeration<String> aliases = clientStore.aliases();
        String aliaz = "";
        while (aliases.hasMoreElements()) {
            aliaz = aliases.nextElement();
            if (clientStore.isKeyEntry(aliaz)) {
                break;
            }
        }

        CMSTypedData msg = new CMSProcessableByteArray(dataToSign); // Data to sign

        X509CertificateHolder x509Certificate = getCert(clientStore, aliaz);
        List<X509CertificateHolder> certList = new ArrayList<>();
        certList.add(x509Certificate); // Adding the X509 Certificate

        Store certs = new JcaCertStore(certList);

        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        // Initializing the the BC's Signer
        ContentSigner sha1Signer = new JcaContentSignerBuilder("PKCS7").setProvider("BC").build(
                getPrivateKey(clientStore, aliaz));

        gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder()
                .setProvider("BC").build()).build(sha1Signer, x509Certificate));
        // adding the certificate
        gen.addCertificates(certs);
        // Getting the signed data
        CMSSignedData sigData = gen.generate(msg, false);
        return sigData.getEncoded();
    }


}
