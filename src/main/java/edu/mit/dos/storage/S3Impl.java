package edu.mit.dos.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import edu.mit.dos.ServiceConfig;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class S3Impl implements StorageManager {

    private static final Logger logger = LoggerFactory.getLogger(S3Impl.class);

    private ServiceConfig serviceConfig;

    private AmazonS3 s3client;

    private S3Utils util;

    @PostConstruct
    public void generate() {
        logger.debug("Service url:{}", serviceConfig.getBucket());

        try {

            s3client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.US_EAST_1)
                    .build();

            logger.debug("Obtained S3 Client");

            util = new S3Utils(s3client);

        } catch (Exception e) {
            logger.error("Exception with S3 client:", e);
        }

    }

    @Override
    public void deleteObject(String key) {
        try {
            util.deleteObject(serviceConfig.getBucket(), key.replace(serviceConfig.getBaseurl(), ""));
        } catch (Exception e) {
            logger.error("Error writing to S3", e);
        }
    }

    @Override
    public String getObject( String key) {
        key = key.replace(serviceConfig.getBaseurl(), "");
        logger.debug("Retrieving binary from S3:{}", key);
        final S3Object obj = util.getObject(serviceConfig.getBucket(), key);
        final S3ObjectInputStream inputStream = obj.getObjectContent();

        try {
            final File tempFile = File.createTempFile("dos-", "-tmp");
            FileUtils.copyInputStreamToFile(inputStream, tempFile);
            return tempFile.getAbsolutePath();
        } catch (IOException e) {
            logger.error("Error downloading file:", e);
        }
        return "";
    }

    /**
     *  Upload an object to S3
     */
    public String putObject(String key, File file) throws IOException{
        final String bucket = serviceConfig.getBucket();
        logger.debug("Putting object in bucket:{} in key:{} for file:{}", bucket, key, file.getPath());

        try {
            final PutObjectResult results = util.putObject(bucket, key, file);
            final String url = s3client.getUrl(bucket, key).toExternalForm();
            return url;

        } catch (SdkClientException e) {
            logger.error("Error writing to S3", e);
            throw new IOException(e);
        }
    }


    @Autowired
    public void setServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }
}
