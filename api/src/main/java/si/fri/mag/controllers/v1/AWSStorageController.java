package si.fri.mag.controllers.v1;

import si.fri.mag.controllers.MainController;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/v1/awsStorage")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AWSStorageController extends MainController {

    @GET
    public Response getBuckets() {

        return this.responseOk("ok", "");
    }
}
