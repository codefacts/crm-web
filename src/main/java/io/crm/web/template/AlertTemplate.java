package io.crm.web.template;

import org.watertemplate.Template;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by sohan on 10/3/2015.
 */
public class AlertTemplate extends Template {
    AlertTemplate(String id, String classes, String body) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        add("body", body);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("alert.html");
    }
}
