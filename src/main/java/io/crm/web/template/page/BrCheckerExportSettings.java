package io.crm.web.template.page;

import io.crm.web.template.Page;
import org.watertemplate.Template;

/**
 * Created by someone on 14/10/2015.
 */
public class BrCheckerExportSettings extends Template {
    public BrCheckerExportSettings(final String exportUrl) {
        add("exportUrl", exportUrl);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("/pages/br-checker-export-settings.html");
    }
}
