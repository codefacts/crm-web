package io.crm.web.template;

import org.watertemplate.Template;

/**
 * Created by someone on 01/10/2015.
 */
public class FileUploadTemplate extends Template {
    @Override
    protected String getFilePath() {
        return "file-upload.html";
    }
}
