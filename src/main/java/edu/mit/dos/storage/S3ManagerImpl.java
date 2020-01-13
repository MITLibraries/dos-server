package edu.mit.dos.storage;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.List;

@Service
public class S3ManagerImpl implements StorageManager {

    private static final Logger logger = LoggerFactory.getLogger(S3ManagerImpl.class);

    private static AWSCredentials credentials;

    @Value("${s3.bucketName:test}")
    private static String bucketName; //TODO to be configured

    @Value("${s3.accessKey:test}")
    private static String accessKey;

    @Value("${s3.secretKey:test}")
    private static String secretKey;

    private static AmazonS3 s3client;

    @PostConstruct
    public void generate() {

    }

    static {
        try {
            credentials = new BasicAWSCredentials(accessKey, secretKey);

            s3client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.US_EAST_2)
                    .build();

            logger.debug("Obtained S3 Client");
        } catch (Exception e) {
            logger.error("Exception:", e);
            credentials = null;
        }
    }

    public String getBucketName() {
        return bucketName;
    }

    public static void setBucketName(String bucketName) {
        S3ManagerImpl.bucketName = bucketName;
    }

    public static String getAccessKey() {
        return accessKey;
    }

    public static void setAccessKey(String accessKey) {
        S3ManagerImpl.accessKey = accessKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static void setSecretKey(String secretKey) {
        S3ManagerImpl.secretKey = secretKey;
    }

    //is bucket exist?
    private boolean doesBucketExist(String bucketName) {
        return s3client.doesBucketExist(bucketName);
    }

    //create a bucket
    private Bucket createBucket(String bucketName) {
        return s3client.createBucket(bucketName);
    }

    //list all buckets
    private List<Bucket> listBuckets() {
        return s3client.listBuckets();
    }

    //delete a bucket
    private void deleteBucket(String bucketName) {
        s3client.deleteBucket(bucketName);
    }

    //uploading object
    public String putObject(String key, File file) {
        PutObjectResult pur;
        try {
            pur = s3client.putObject(bucketName, key, file);
        } catch (SdkClientException e) {
            logger.error("Error writing to S3", e);
            return "";
        }
        return pur.getContentMd5();
    }

    //listing objects
    private ObjectListing listObjects(String bucketName) {
        return s3client.listObjects(bucketName);
    }

    //get an object
    private S3Object getObject(String bucketName, String objectKey) {
        return s3client.getObject(bucketName, objectKey);
    }

    //copying an object
    private CopyObjectResult copyObject(
            String sourceBucketName,
            String sourceKey,
            String destinationBucketName,
            String destinationKey
    ) {
        return s3client.copyObject(
                sourceBucketName,
                sourceKey,
                destinationBucketName,
                destinationKey
        );
    }

    //deleting an object
    private void deleteObject(String bucketName, String objectKey) {
        s3client.deleteObject(bucketName, objectKey);
    }

    //deleting multiple Objects
    private DeleteObjectsResult deleteObjects(DeleteObjectsRequest delObjReq) {
        return s3client.deleteObjects(delObjReq);
    }

}
