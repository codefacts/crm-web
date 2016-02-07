package io.crm.web.template;

import io.crm.util.Util;
import org.watertemplate.Template;

/**
 * Created by someone on 12/10/2015.
 */
final public class ExportButton extends Template {
    ExportButton(final String exportLink, final String clasz, final String label) {
        add("exportLink", Util.or(exportLink, ""));
        add("class", Util.or(clasz, ""));
        add("label", Util.or(label, "Export"));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("export-button.html");
    }
}
