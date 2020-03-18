package si.fri.mag.controllers.v1;

import com.amazonaws.services.s3.model.Bucket;
import si.fri.mag.BucketStorageService;
import si.fri.mag.controllers.MainController;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("/v1/awsStorage/bucket")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AWSStorageBucketController extends MainController {

    @Inject
    private BucketStorageService bucketStorageService;

    @GET
    @Path("list")
    public Response getBuckets() {
        List<Bucket> buckets = bucketStorageService.listBuckets();
        return this.responseOk("", buckets);
    }

    @POST
    @Path("{bucketName}")
    public Response createBucket(@PathParam("bucketName") String bucketName) {
        Bucket bucket = bucketStorageService.createBucket(bucketName);
        return this.responseOk("bucket created", bucket);
    }

    @DELETE
    @Path("{bucketName}")
    public Response deleteBucket(@PathParam("bucketName") String bucketName) {
        boolean isDeleted = bucketStorageService.deleteBucket(bucketName);
        return isDeleted ?
                this.responseOk("Bucket: " + bucketName , "ok") :
                this.responseError(400,"Bucket: " + bucketName + " not exist");
    }
}
