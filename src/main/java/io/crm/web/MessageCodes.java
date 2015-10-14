package io.crm.web;

/**
 * Created by someone on 14/10/2015.
 */
public enum MessageCodes {
    fileuploadSuccess("fileupload.success", "File uploaded successfully."),
    unknownError("unknown_error", "Unknown Error."),
    file_already_exists("file.already_exists", "File already exists."),
    filename_invalid("filename.invalid", "File name is invalid.");

    public final String code;
    public final String message;

    MessageCodes(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
