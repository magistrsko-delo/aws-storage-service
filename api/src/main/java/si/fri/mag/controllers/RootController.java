package si.fri.mag.controllers;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RootController extends MainController {
    @GET
    public Response getRoot() {
        throw new ForbiddenException("Forbidden path");
    }
}
