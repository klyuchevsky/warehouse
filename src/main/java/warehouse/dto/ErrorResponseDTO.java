package warehouse.dto;

public class ErrorResponseDTO {
    private final String method;
    private final String uri;
    private final String message;

    public ErrorResponseDTO(String method, String uri, String message) {
        this.method = method;
        this.uri = uri;
        this.message = message;
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getMessage() {
        return message;
    }

}