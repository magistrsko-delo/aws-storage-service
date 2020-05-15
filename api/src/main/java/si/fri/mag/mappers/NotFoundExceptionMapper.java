package si.fri.mag.mappers;

import si.fri.mag.DTO.responses.ResponseErrorDTO;
import si.fri.mag.controllers.MainController;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpUtils;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper extends MainController implements ExceptionMapper<NotFoundException> {
    @Context
    private HttpServletRequest request;

    @Override
    public Response toResponse(NotFoundException e) {
        final StringBuffer absolutePath = HttpUtils.getRequestURL(request);
        return this.responseError(404, e.getMessage() + "  : " + absolutePath);
    }
}
