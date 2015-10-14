package io.crm.web.util;

/**
 * Created by someone on 14/10/2015.
 */
public class UploadResult {
    private final Status status;
    private final String message;
    private final String messageCode;
    private final String filename;

    public UploadResult(Status status, String message, String messageCode, String filename) {
        this.status = status;
        this.message = message;
        this.messageCode = messageCode;
        this.filename = filename;
    }

    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getFilename() {
        return filename;
    }

    public String getMessageCode() {
        return messageCode == null ? "" : messageCode;
    }
}
