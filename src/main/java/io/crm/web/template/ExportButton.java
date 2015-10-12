package io.crm.web.template;

import io.crm.util.Util;
import org.watertemplate.Template;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 12/10/2015.
 */
final public class ExportButton extends Template {
    ExportButton(final String exportLink, final String clasz, final String label) {
        add("exportLink", getOrDefault(exportLink, ""));
        add("class", getOrDefault(clasz, ""));
        add("label", getOrDefault(label, "Export"));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("export-button.html");
    }
}
