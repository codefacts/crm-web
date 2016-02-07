package io.crm.web.template.input;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

/**
 * Created by someone on 30/09/2015.
 */
public class ButtonTemplate extends Template {
    ButtonTemplate(final String id, final String classes, final String btnText, final String name, final String value) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        add("buttonText", Util.or(btnText, ""));
        add("name", name);
        add("value", value);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("button.html");
    }
}
