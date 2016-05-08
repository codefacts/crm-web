package webcomposer;

/**
 * Created by shahadat on 5/8/16.
 */
public final class ErrorRsp {
    public final int code;
    public final String messageCode;
    public final int httpResponseCode;

    private ErrorRsp(int code, String messageCode, int httpResponseCode) {
        this.code = code;
        this.messageCode = messageCode;
        this.httpResponseCode = httpResponseCode;
    }

    public static ErrorRspBuilder builder() {
        return new ErrorRspBuilder();
    }

    public static ErrorRsp create(int code, String messageCode, int httpResponseCode) {
        return new ErrorRsp(code, messageCode, httpResponseCode);
    }
}
