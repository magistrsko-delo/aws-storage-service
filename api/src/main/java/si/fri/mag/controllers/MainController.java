package si.fri.mag.controllers;

import si.fri.mag.DTO.responses.ResponseErrorDTO;
import si.fri.mag.DTO.responses.ResponseOkDTO;

import javax.ws.rs.core.Response;

public class MainController {
    public Response responseOk(String message, Object data) {
        return Response.status(200).entity(new ResponseOkDTO(200, message, data)).build();
    }

    public Response responseError(Integer status, String message) {
        return Response.status(status).entity(new ResponseErrorDTO(status, message)).build();
    }
}
