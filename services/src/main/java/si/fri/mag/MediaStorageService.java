package si.fri.mag;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.*;

import si.fri.mag.util.AmazonClient;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.InternalServerErrorException;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class MediaStorageService extends AmazonClient {

    public boolean uploadMedia(InputStream inputStream, String bucketName, String mediaName) {
        try {
            System.out.println("uploading media");
            ObjectMetadata fileMetadata = new ObjectMetadata();
            fileMetadata.setContentLength(inputStream.available());
            s3client.putObject(bucketName, mediaName, inputStream, fileMetadata);
            return true;
        } catch (AmazonServiceException | IOException e) {
            e.printStackTrace();
            throw new InternalServerErrorException(e.getMessage());
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

    public S3ObjectInputStream getMedia(String bucketName, String mediaName) {
        try {
            S3Object s3object = s3client.getObject(bucketName, mediaName);
            return s3object.getObjectContent();
        } catch (AmazonServiceException e) {
            return null;
        }

    }

}
