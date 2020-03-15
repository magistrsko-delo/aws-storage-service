package si.fri.mag.DTO.responses;

public class ResponseOkDTO extends ResponseAbstract {
    private Object data;

    public ResponseOkDTO(Integer status, String message, Object data) {
        super(status, message);
        this.data = data;
    }

    public Object getData() {
        return this.data;
    }
}
