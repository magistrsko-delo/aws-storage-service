package si.fri.mag.DTO.responses;

public class ResponseErrorDTO extends ResponseAbstract {
    public ResponseErrorDTO(Integer status, String message) {
        super(status, message);
    }
}
