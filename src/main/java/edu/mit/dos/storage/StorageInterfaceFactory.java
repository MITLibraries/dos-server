package edu.mit.dos.storage;

import edu.mit.dos.ServiceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Factory decides which class to inject depending on system property.
 * If "storage=s3" in the properties file, it will not write to S3 (o/wise to the filesystem).
 */
@Component
public class StorageInterfaceFactory {

    private static final Logger logger = LoggerFactory.getLogger(LocalFileSystemImpl.class);

    @Autowired
    private ServiceConfig serviceConfig;

    @Autowired
    @Qualifier("s3Impl")
    private StorageManager s3Impl;

    @Autowired
    @Qualifier("localFileSystemImpl")
    private StorageManager localImpl;

    public StorageManager getInstance() {

        if (serviceConfig == null) {
            logger.error("Couldn't inject properties!!");
        }

        if (serviceConfig.getStorage().equalsIgnoreCase("s3")) {
            logger.debug("Returning s3 for storage");
            return s3Impl; // s3
        } else {
            logger.debug("Returning local filesystem for storage");
            return localImpl; // file system
        }
    }

    public ServiceConfig getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }
}