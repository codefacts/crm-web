package io.crm.web.template.bootstrap;

import org.watertemplate.Template;

/**
 * Created by someone on 13/10/2015.
 */
final public class PanelHeader extends Template {

    PanelHeader(final String clasz, final String title_class, final String title, final String body) {
        add("class", clasz);
        add("title_class", title_class);
        add("title", title);
        add("body", body);
    }

    @Override
    protected String getFilePath() {
        return "panel-header.html";
    }
}
