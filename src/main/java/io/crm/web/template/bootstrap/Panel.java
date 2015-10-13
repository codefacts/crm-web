package io.crm.web.template.bootstrap;

import org.watertemplate.Template;

/**
 * Created by someone on 13/10/2015.
 */
final public class Panel extends Template {
    Panel(final String clasz, final String body_class,
          final String panel_header, final String panel_footer, final String body) {
        add("class", clasz);
        add("body_class", body_class);
        add("panel_header", panel_header);
        add("panel_footer", panel_footer);
        add("body", body);
    }

    @Override
    protected String getFilePath() {
        return "panel.html";
    }
}
