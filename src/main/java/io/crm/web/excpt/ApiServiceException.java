package io.crm.web.excpt;

/**
 * Created by someone on 21/09/2015.
 */
public class ApiServiceException extends Exception {
    private final int code;

    public ApiServiceException(final int code, final String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
