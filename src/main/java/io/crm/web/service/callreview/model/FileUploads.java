package io.crm.web.service.callreview.model;

/**
 * Created by someone on 15/10/2015.
 */
public class FileUploads {
    public static final String id = "id";
    public static final String filename = "filename";
    public static final String uploaded_filename = "uploaded_filename";
    public static final String upload_date = "upload_date";
    public static final String status = "status";
    public static final String errorDetails = "errorDetails";

    public enum Statuses {
        initial, complete, error, dateformatError
    }
}
