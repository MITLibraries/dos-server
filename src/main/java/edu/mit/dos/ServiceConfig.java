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
@ConfigurationProperties(prefix="config")
public class ServiceConfig {

    @NotNull
    private String bucket;

    @NotNull
    private String storage;

    @NotNull
    private String minter;

    @NotNull
    private String baseurl;

    @NotNull
    private String mode;

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

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}