package grpc;

import com.amazonaws.services.s3.model.Bucket;
import com.kumuluz.ee.grpc.annotations.GrpcService;
import io.grpc.stub.StreamObserver;
import si.fri.mag.BucketStorageService;
import si.fri.mag.MediaStorageService;

import javax.enterprise.inject.spi.CDI;
import java.io.*;

@GrpcService
public class AwsStorageServiceImpl extends AwsstorageGrpc.AwsstorageImplBase {
    private BucketStorageService bucketStorageService;
    private MediaStorageService mediaStorageService;
    private int mStatus = 200;

    String bucketname = "";
    String medianame = "";
    private BufferedOutputStream mBufferedOutputStream = null;

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

    @Override
    public StreamObserver<AwsstorageService.UploadRequest> uploadFile(StreamObserver<AwsstorageService.UploadResponse> responseObserver) {
        return new StreamObserver<AwsstorageService.UploadRequest>() {
            int mmCount = 0;

            @Override
            public void onNext(AwsstorageService.UploadRequest uploadRequest) {
                // Print count
                System.out.println("onNext count: " + mmCount);
                mmCount++;
                byte[] data = uploadRequest.getData().toByteArray();
                long offset = uploadRequest.getOffset();
                bucketname = uploadRequest.getBucketname();
                medianame = uploadRequest.getMedianame();
                try {
                    if (mBufferedOutputStream == null) {
                        mBufferedOutputStream = new BufferedOutputStream(new FileOutputStream(medianame));
                    }
                    mBufferedOutputStream.write(data);
                    mBufferedOutputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("ERROR: " + throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                if (mBufferedOutputStream != null) {
                    try {
                        mBufferedOutputStream.close();
                        mediaStorageService = CDI.current().select(MediaStorageService.class).get();

                        final File file = new File(medianame);
                        final InputStream inputStream = new DataInputStream(new FileInputStream(file));
                        boolean isUploaded = mediaStorageService.uploadMedia(inputStream, bucketname, medianame);
                        file.delete();

                        responseObserver.onNext(AwsstorageService.UploadResponse.newBuilder().setStatus(mStatus).setMessage("is uploaded: " + isUploaded).build());
                        responseObserver.onCompleted();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        mBufferedOutputStream = null;
                    }
                }
            }
        };
    }
}
