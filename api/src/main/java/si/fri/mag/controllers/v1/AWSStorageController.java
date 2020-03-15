package si.fri.mag.controllers.v1;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.glassfish.jersey.media.multipart.FormDataParam;
import si.fri.mag.MediaStorageService;
import si.fri.mag.controllers.MainController;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.List;

@ApplicationScoped
@Path("/v1/awsStorage")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AWSStorageController extends MainController {

    @Inject
    private MediaStorageService mediaStorageService;

    @GET
    @Path("buckets")
    public Response getBuckets() {
        List<Bucket> buckets = mediaStorageService.listBuckets();
        return this.responseOk("", buckets);
    }

    @POST
    @Path("buckets/{bucketName}")
    public Response createBucket(@PathParam("bucketName") String bucketName) {
        Bucket bucket = mediaStorageService.createBucket(bucketName);
        return this.responseOk("bucket created", bucket);
    }

    @DELETE
    @Path("buckets/{bucketName}")
    public Response deleteBucket(@PathParam("bucketName") String bucketName) {
        boolean isDeleted = mediaStorageService.deleteBucket(bucketName);
        return isDeleted ?
                this.responseOk("Bucket: " + bucketName , "ok") :
                this.responseError(400,"Bucket: " + bucketName + " not exist");
    }

    @POST
    @Path("/media/{bucketName}/{mediaName}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadFile(
            @FormDataParam("mediaStream") InputStream mediaStream,
            @PathParam("bucketName") String bucketName,
            @PathParam("mediaName") String mediaName) {

        boolean isUploaded = mediaStorageService.uploadMedia(mediaStream, bucketName, mediaName);
        if (!isUploaded) {
            return this.responseError(500, "File upload error");
        }
        return this.responseOk("media uploaded", isUploaded);
    }

    @GET
    @Path("/media/{bucketName}")
    public Response getMediaMetadataInBucket(@PathParam("bucketName") String bucketName) {
        ObjectListing mediaMetadata = mediaStorageService.getMediaMetadataInBucket(bucketName);
        if(mediaMetadata == null)
            return this.responseError(404, "Bucket with given name does not exist");
        else
            return this.responseOk("", mediaMetadata);
    }

    @DELETE
    @Path("/media/{bucketName}/{mediaName}")
    public Response deleteMedia(@PathParam("bucketName") String bucketName, @PathParam("mediaName") String mediaName) {
        boolean isDeleted = mediaStorageService.deleteMedia(bucketName, mediaName);
        return isDeleted ?
                this.responseOk("Media: " + bucketName + "/" + mediaName, "ok") :
                this.responseError(400,"Media: " + bucketName + "/" + mediaName);
    }

    @GET
    @Path("/media/{bucketName}/{mediaName}")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response getMedia(@PathParam("bucketName") String bucketName, @PathParam("mediaName") String mediaName) {
        S3ObjectInputStream outputStream = mediaStorageService.getMedia(bucketName, mediaName);
        return Response
                .status(outputStream == null ? 404 : 200)
                .entity(outputStream == null ? "Error: File " + bucketName + "/" + mediaName + " not found" : outputStream)
                .build();
    }
}
