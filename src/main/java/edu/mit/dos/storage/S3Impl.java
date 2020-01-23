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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;

@Service
public class S3Impl implements StorageManager {

    private static final Logger logger = LoggerFactory.getLogger(S3Impl.class);

    private ServiceConfig serviceConfig;

    private AmazonS3 s3client;

    private S3Utils util;

    @PostConstruct
    public void generate() {

        logger.debug("Service username:{}", serviceConfig.getAccessKey());
        logger.debug("Service password:{}", serviceConfig.getSecretKey());
        logger.debug("Service url:{}", serviceConfig.getBucket());

        try {
            final AWSCredentials credentials =
                    new BasicAWSCredentials(serviceConfig.getAccessKey(), serviceConfig.getSecretKey());

            s3client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.US_EAST_1)
                    .build();

            logger.debug("Obtained S3 Client");

            util = new S3Utils(s3client);

        } catch (Exception e) {
            logger.error("Exception with S3 client:", e);
        }

    }

    /**
     *  Upload an object to S3
     */
    public String putObject(String key, File file) {

        logger.debug("Putting object in bucket:{} in key:{} for file:{}", serviceConfig.getBucket(), key, file.getPath());

        try {
            final PutObjectResult results = util.putObject(serviceConfig.getBucket(), key, file);
            return results.getContentMd5();

        } catch (SdkClientException e) {
            logger.error("Error writing to S3", e);
            return "";
        }
    }


    @Autowired
    public void setServiceConfig(ServiceConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }
}