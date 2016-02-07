package io.crm.web.template;

import io.crm.util.SimpleCounter;
import io.crm.util.Util;
import io.crm.web.util.Status;
import io.crm.web.util.UploadResult;
import org.watertemplate.Template;

import java.util.Collections;
import java.util.List;

/**
 * Created by someone on 13/10/2015.
 */
public class FileUploadStatusTable extends Template {
    public FileUploadStatusTable(List<UploadResult> results) {
        results = Util.or(results, Collections.EMPTY_LIST);
        final SimpleCounter counter = new SimpleCounter(1);
        results.stream().sorted(((o1, o2) -> o1.getMessageCode().compareTo(o2.getMessageCode())));
        addCollection("rows", results, (rs, obj) -> {
            obj.add("class", rs.getStatus() == Status.success ? "active" : "warning");
            obj.add("row_no", (counter.counter++) + "");
            obj.add("fileName", rs.getFilename());
            obj.add("status", rs.getStatus().name().toUpperCase() + ": " + rs.getMessageCode());
            obj.add("message", rs.getMessage());
        });
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("file-upload-status-table.html");
    }
}
