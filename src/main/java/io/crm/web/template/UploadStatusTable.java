package io.crm.web.template;

import io.crm.util.SimpleCounter;
import io.crm.util.Util;
import io.crm.web.controller.ImageUploadController;
import org.watertemplate.Template;

import java.util.Collections;
import java.util.List;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 13/10/2015.
 */
public class UploadStatusTable extends Template {
    public UploadStatusTable(List<ImageUploadController.UploadResult> results) {
        results = getOrDefault(results, Collections.EMPTY_LIST);
        final SimpleCounter counter = new SimpleCounter(1);
        addCollection("rows", results, (rs, obj) -> {
            obj.add("class", rs.getStatus() == ImageUploadController.Status.success ? "active" : "warning");
            obj.add("row_no", (counter.counter++) + "");
            obj.add("fileName", rs.getFilename());
            obj.add("status", rs.getStatus().name());
            obj.add("message", rs.getMessage());
        });
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("upload-status-table.html");
    }
}
