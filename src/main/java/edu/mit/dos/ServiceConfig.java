package edu.mit.dos;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;

/**
 * Used to configure properties.
 */
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix="serviceConfig")
public class ServiceConfig {

    @NotNull
    private String accessKey;

    @NotNull
    private String secretKey;

    @NotNull
    private String bucket;

    @NotNull
    private String storage;

    @NotNull
    private String minter;

    @NotNull
    private String baseurl;

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getMinter() {
        return minter;
    }

    public void setMinter(String minter) {
        this.minter = minter;
    }

    public String getBaseurl() {
        return baseurl;
    }

    public void setBaseurl(String baseurl) {
        this.baseurl = baseurl;
    }
}