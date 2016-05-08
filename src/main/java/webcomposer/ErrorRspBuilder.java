package webcomposer;

public class ErrorRspBuilder {
    private int code;
    private String messageCode;
    private int httpResponseCode;

    public ErrorRspBuilder setCode(int code) {
        this.code = code;
        return this;
    }

    public ErrorRspBuilder setMessageCode(String messageCode) {
        this.messageCode = messageCode;
        return this;
    }

    public ErrorRspBuilder setHttpResponseCode(int httpResponseCode) {
        this.httpResponseCode = httpResponseCode;
        return this;
    }

    public ErrorRsp createErrorRsp() {
        return ErrorRsp.create(code, messageCode, httpResponseCode);
    }
}