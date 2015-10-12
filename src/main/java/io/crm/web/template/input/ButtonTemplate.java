package io.crm.web.template.input;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 30/09/2015.
 */
public class ButtonTemplate extends Template {
    ButtonTemplate(final String id, final String classes, final String btnText, final String name, final String value) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        add("buttonText", getOrDefault(btnText, ""));
        add("name", name);
        add("value", value);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("button.html");
    }
}
