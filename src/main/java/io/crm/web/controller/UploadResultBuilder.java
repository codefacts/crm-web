package io.crm.web.controller;

public class UploadResultBuilder {
    private ImageUploadController.Status status;
    private String message;
    private String filename;

    public UploadResultBuilder setStatus(ImageUploadController.Status status) {
        this.status = status;
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

    public ImageUploadController.UploadResult createUploadResult() {
        return new ImageUploadController.UploadResult(status, message, filename);
    }
}