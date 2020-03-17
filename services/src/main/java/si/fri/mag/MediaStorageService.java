package si.fri.mag;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;
import java.io.InputStream;
import java.util.List;
import java.util.Random;

@RequestScoped
public class MediaStorageService {

    @Inject
    private AppProperties appProperties;

    private AmazonS3 s3client;

    private Random randomNumberGenerator = new Random();

    @PostConstruct
    void init() {
        try{
            AWSCredentials credentials = new BasicAWSCredentials(
                    appProperties.getAwsAccessKey(),
                    appProperties.getAwsSecretKey()
            );

            s3client = AmazonS3ClientBuilder
                    .standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withRegion(Regions.EU_CENTRAL_1)
                    .build();

        } catch (Exception e) {
            throw new AmazonClientException("Cannot initialize credentials.", e);
        }
    }

    public List<Bucket> listBuckets(){
        return s3client.listBuckets();
    }

    public Bucket createBucket(String bucketName) {
        bucketName = bucketName.toLowerCase();
        while(s3client.doesBucketExistV2(bucketName)){
            bucketName += Integer.toString(randomNumberGenerator.nextInt(9));
        }
        return  s3client.createBucket(bucketName);
    }

    public boolean uploadMedia(InputStream inputStream, String bucketName, String mediaName) {
        try {
            s3client.putObject(bucketName, mediaName, inputStream, new ObjectMetadata());
            return true;
        } catch (AmazonServiceException e) {
            throw new InternalServerErrorException(e.getErrorMessage());
        }
    }

    public ObjectListing getMediaMetadataInBucket(String bucketName){
        if(!s3client.doesBucketExistV2(bucketName)){
            return null;
        }
        return s3client.listObjects(bucketName);
    }

    public boolean deleteMedia(String bucketName, String mediaName) {
        if(!s3client.doesBucketExistV2(bucketName)) {
            return false;
        }
        try {
            s3client.deleteObject(bucketName, mediaName);
            return true;
        } catch (AmazonServiceException e) {
            throw new InternalServerErrorException(e.getErrorMessage());
        }
    }

    public boolean deleteBucket(String bucketName) {
        if(!s3client.doesBucketExistV2(bucketName)){
            return false;
        }

        try {
            s3client.deleteBucket(bucketName);
            return true;
        } catch (AmazonServiceException e) {
            throw new InternalServerErrorException(e.getErrorMessage());
        }
    }

    public S3ObjectInputStream getMedia(String bucketName, String mediaName) {
        try {
            S3Object s3object = s3client.getObject(bucketName, mediaName);
            return s3object.getObjectContent();
        } catch (AmazonServiceException e) {
            return null;
        }

    }

}
