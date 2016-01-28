package com.xmas.service.notifiers.safari.pushpackage;

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

    private X509CertificateHolder getCert(KeyStore keystore, String alias) throws GeneralSecurityException, IOException {
        java.security.cert.Certificate c = keystore.getCertificate(alias);
        return new X509CertificateHolder(c.getEncoded());
    }

    private PrivateKey getPrivateKey(KeyStore keystore, String alias) throws GeneralSecurityException, IOException {
        return (PrivateKey) keystore.getKey(alias, password.toCharArray());
    }

    public byte[] sign(byte[] dataToSign) throws IOException, GeneralSecurityException, OperatorCreationException, CMSException {
        KeyStore clientStore = getKeystore();
        assert clientStore != null;
        Enumeration<String> aliases = clientStore.aliases();
        String alias = null;
        while (aliases.hasMoreElements()) {
            alias = aliases.nextElement();
            if (clientStore.isKeyEntry(alias)) {
                break;
            }
        }

        CMSTypedData msg = new CMSProcessableByteArray(dataToSign); // Data to sign

        X509CertificateHolder x509Certificate = getCert(clientStore, alias);
        List<X509CertificateHolder> certList = new ArrayList<>();
        certList.add(x509Certificate); // Adding the X509 Certificate

        Store certs = new JcaCertStore(certList);

        CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
        // Initializing the the BC's Signer
        ContentSigner sha256Signer = new JcaContentSignerBuilder("SHA1withRSA").setProvider("BC").build(
                getPrivateKey(clientStore, alias));

        gen.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder()
                .setProvider("BC").build()).build(sha256Signer, x509Certificate));
        gen.addCertificates(certs);
        CMSSignedData sigData = gen.generate(msg, false);
        return sigData.getEncoded();
    }


}
