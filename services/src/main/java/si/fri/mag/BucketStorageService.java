package si.fri.mag;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.Bucket;
import si.fri.mag.util.AmazonClient;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.InternalServerErrorException;
import java.util.List;
import java.util.Random;

@RequestScoped
public class BucketStorageService extends AmazonClient {

    private Random randomNumberGenerator = new Random();

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
}
