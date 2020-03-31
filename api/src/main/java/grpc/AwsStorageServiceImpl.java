package grpc;

import com.amazonaws.services.s3.model.Bucket;
import com.kumuluz.ee.grpc.annotations.GrpcService;
import io.grpc.stub.StreamObserver;
import si.fri.mag.BucketStorageService;

import javax.enterprise.inject.spi.CDI;

@GrpcService
public class AwsStorageServiceImpl extends AwsstorageGrpc.AwsstorageImplBase {
    private BucketStorageService bucketStorageService;

    @Override
    public void createBucket(AwsstorageService.CreateBucketRequest request, StreamObserver<AwsstorageService.CreateBucketResponse> responseObserver){
        bucketStorageService = CDI.current().select(BucketStorageService.class).get();

        Bucket bucket = bucketStorageService.createBucket(request.getBucketname());

        AwsstorageService.CreateBucketResponse response;
        response = AwsstorageService.CreateBucketResponse.newBuilder()
                .setBucketname(bucket.getName())
                .build();
        responseObserver.onNext(response);

        responseObserver.onCompleted();

    }
}
