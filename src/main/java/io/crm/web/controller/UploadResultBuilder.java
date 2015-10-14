package io.crm.web.controller;

import io.crm.web.MessageCodes;
import io.crm.web.util.Status;
import io.crm.web.util.UploadResult;

public class UploadResultBuilder {
    private Status status;
    private String message;
    private String filename;
    private String messageCode;

    public UploadResultBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    public UploadResultBuilder setMessageCode(MessageCodes messageCode) {
        this.messageCode = messageCode.code;
        this.message = messageCode.message;
        return this;
    }

    public UploadResultBuilder setMessageCode(String messageCode) {
        this.messageCode = messageCode;
        return this;
    }

    public UploadResultBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public UploadResultBuilder setFilename(String filename) {
        this.filename = filename;
        return this;
    }

    public UploadResult createUploadResult() {
        return new UploadResult(status, message, messageCode, filename);
    }
}