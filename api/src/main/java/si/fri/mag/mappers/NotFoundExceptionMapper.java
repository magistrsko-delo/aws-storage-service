package si.fri.mag.mappers;

import si.fri.mag.DTO.responses.ResponseErrorDTO;
import si.fri.mag.controllers.MainController;


import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper extends MainController implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException e) {
        return this.responseError(404, e.getMessage());
    }
}
