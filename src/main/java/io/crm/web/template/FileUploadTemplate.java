package io.crm.web.template;

import org.watertemplate.Template;

/**
 * Created by someone on 01/10/2015.
 */
public class FileUploadTemplate extends Template {
    public FileUploadTemplate(final String statusContent) {
        add("statusContent", statusContent);
    }

    @Override
    protected String getFilePath() {
        return "file-upload.html";
    }
}
