package edu.mit.dos.storage;

import com.amazonaws.auth.AWSCredentials;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class LocalFileSystemManagerImpl implements StorageManager {

    private static final Logger logger = LoggerFactory.getLogger(LocalFileSystemManagerImpl.class);

    private static AWSCredentials credentials;

    private static String bucketName; //TODO to be configured

    @Value("${s3.accessKey:test}")
    private static String accessKey;

    @Value("${s3.secretKey:test}")
    private static String secretKey;

    private static File tempDirPath;

    static {
        try {
            tempDirPath = createTempDirectory();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String putObject(String key, File file) {
        try {
            final File f = new File( tempDirPath + File.separator + "dos" + key);
            FileUtils.copyFile(file, f); // copies file to specific path on disk
            logger.debug("Copied to:{}", f.getAbsolutePath());
            return f.getPath();
        } catch (IOException e) {
            logger.error("Error copying file:", e);
        }
        return "";
    }

    public  String getBucketName() {
        return tempDirPath.getAbsolutePath();
    }

    public static void setBucketName(String bucketName) {
        LocalFileSystemManagerImpl.bucketName = bucketName;
    }

    public static String getAccessKey() {
        return accessKey;
    }

    public static void setAccessKey(String accessKey) {
        LocalFileSystemManagerImpl.accessKey = accessKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static void setSecretKey(String secretKey) {
        LocalFileSystemManagerImpl.secretKey = secretKey;
    }

    public static File createTempDirectory()
            throws IOException {
        final File temp;

        temp = File.createTempFile("temp", Long.toString(System.nanoTime()));

        if (!(temp.delete())) {
            throw new IOException("Could not delete temp file: " + temp.getAbsolutePath());
        }

        if (!(temp.mkdir())) {
            throw new IOException("Could not create temp directory: " + temp.getAbsolutePath());
        }

        return (temp);
    }
}
