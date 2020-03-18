package si.fri.mag.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import si.fri.mag.AppProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.InternalServerErrorException;

public class AmazonClient {
    @Inject
    private AppProperties appProperties;

    protected AmazonS3 s3client;

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
            throw new InternalServerErrorException(e.getMessage());
        }
    }
}
