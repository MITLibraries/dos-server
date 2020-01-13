package edu.mit.dos.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Factory decides which class to inject depending on system property.
 * If "storage=s3" it will not write to S3 (o/wise to the filesystem).
 */
@Component
public class StorageInterfaceFactory {

    private static String testing = System.getProperty("storage"); //TODO nicer config

    @Autowired
    @Qualifier("s3ManagerImpl")
    private StorageManager s3Impl;

    @Autowired
    @Qualifier("localFileSystemManagerImpl")
    private StorageManager localImpl;

    public StorageManager getInstance(){
        if (testing.equalsIgnoreCase("s3")) // local file system only
            return s3Impl;
        else
            return localImpl; // S3
    }

}