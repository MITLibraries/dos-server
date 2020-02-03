package edu.mit.dos.storage;

import java.io.File;
import java.util.List;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.stereotype.Service;

@Service
public class S3Utils {

    private final AmazonS3 s3client;

    public S3Utils() {
        this(new AmazonS3Client() {
        });
    }

    public S3Utils(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    public boolean doesBucketExist(String bucketName) {
        return s3client.doesBucketExist(bucketName);
    }

    public Bucket createBucket(String bucketName) {
        return s3client.createBucket(bucketName);
    }

    public List<Bucket> listBuckets() {
        return s3client.listBuckets();
    }

    public void deleteBucket(String bucketName) {
        s3client.deleteBucket(bucketName);
    }

    public PutObjectResult putObject(String bucketName, String key, File file) {
        return s3client.putObject(bucketName, key, file);
    }

    public ObjectListing listObjects(String bucketName) {
        return s3client.listObjects(bucketName);
    }

    public S3Object getObject(String bucketName, String objectKey) {
        return s3client.getObject(bucketName, objectKey);
    }

    public CopyObjectResult copyObject(
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

    public void deleteObject(String bucketName, String objectKey) {
        s3client.deleteObject(bucketName, objectKey);
    }

    public DeleteObjectsResult deleteObjects(DeleteObjectsRequest delObjReq) {
        return s3client.deleteObjects(delObjReq);
    }

}
