package io.crm.web.template.bootstrap;

import io.crm.web.template.Page;
import org.watertemplate.Template;

/**
 * Created by someone on 13/10/2015.
 */
public class BodyPanelDefault extends Template {
    BodyPanelDefault(final String body, final String body_class) {
        add("body", body);
        add("body_class", body_class);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("body-panel-default.html");
    }
}
