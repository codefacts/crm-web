package io.crm.web.template;

import io.crm.util.Util;
import io.crm.web.template.bootstrap.BodyPanelDefaultBuilder;
import io.crm.web.util.Status;
import io.crm.web.util.UploadResult;
import org.watertemplate.Template;

import java.util.Collections;
import java.util.List;

/**
 * Created by someone on 13/10/2015.
 */
final public class ImageUploadForm extends Template {
    public ImageUploadForm(List<UploadResult> results) {
        results = Util.or(results, Collections.EMPTY_LIST);
        final long success = results.stream()
                .filter(r -> r.getStatus() == Status.success)
                .count();
        final int error = (int) (results.size() - success);

        add("status", results.size() <= 0 ? ""
                : new BodyPanelDefaultBuilder()
                .addBodyClass(results.size() <= 0 ? "" : (error <= 0 ? "bg-success" : "bg-warning"))
                .setBody(String.format("Success: %d, Fail: %d", success, error))
                .createBodyPanelDefault().render());

        add("statusTable", results.size() <= 0 ? ""
                : new BodyPanelDefaultBuilder()
                .setBody(new FileUploadStatusTable(results).render())
                .createBodyPanelDefault().render());
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("image-upload-form.html");
    }
}
