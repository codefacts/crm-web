package io.crm.web.template;

import org.watertemplate.Template;

/**
 * Created by someone on 23/09/2015.
 */
public class FiltersPanelTemplate extends Template {
    public FiltersPanelTemplate(final String title) {
        add("title", title);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("filters-panel.html");
    }
}
