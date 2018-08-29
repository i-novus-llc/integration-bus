package ru.i_novus.integration.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Service
@PropertySource("file:${app.home}/placeholders.properties")
public class PlaceholdersProperty {
    @Value("${sign.soap.key.store.file.path}")
    String keyStorePath;
    @Value("${sign.soap.key.store.password}")
    String keyStorePassword;
    @Value("${sign.soap.key.store.region.alias.name}")
    String keyStoreAlias;
    @Value("${sign.soap.key.store.region.alias.password}")
    String keyStoreAliasPassword;
    @Value("${file.storage.temp.path}")
    String tempPath;
    @Value("${registry.address}")
    String registryAddress;

    KeyStore keyStore;

    public KeyStore getKeyStore() {
        if (keyStore == null) {
            try {
                keyStore = KeyStore.getInstance("JKS");
                keyStore.load(this.getClass().getClassLoader().getResourceAsStream("getKeyStorePath()"), getKeyStorePassword().toCharArray());
            } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            }
        }
        return keyStore;
    }

    public String getKeyStorePath() {
        return keyStorePath;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public String getKeyStoreAlias() {
        return keyStoreAlias;
    }

    public String getKeyStoreAliasPassword() {
        return keyStoreAliasPassword;
    }

    public String getTempPath() {
        return tempPath;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }
}
