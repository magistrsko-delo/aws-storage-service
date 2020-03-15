package si.fri.mag.DTO.responses;

public abstract class ResponseAbstract {
    protected Integer status;
    protected String message;
    ResponseAbstract(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return this.status;
    }
    public String getMessage() {
        return this.message;
    }
}
