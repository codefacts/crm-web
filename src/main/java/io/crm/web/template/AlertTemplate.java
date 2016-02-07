package io.crm.web.template;

import io.crm.util.Util;
import org.watertemplate.Template;

/**
 * Created by sohan on 10/3/2015.
 */
public class AlertTemplate extends Template {
    AlertTemplate(String id, String classes, String body) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        add("body", body);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("alert.html");
    }
}
