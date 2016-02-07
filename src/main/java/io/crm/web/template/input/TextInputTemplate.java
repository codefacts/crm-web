package io.crm.web.template.input;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

/**
 * Created by someone on 30/09/2015.
 */
public class TextInputTemplate extends Template {

    public TextInputTemplate(String id, String classes, String name, String value, String placeholder) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        add("name", Util.or(name, ""));
        add("value", Util.or(value, ""));
        add("placeholder", Util.or(placeholder, ""));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("text-input.html");
    }
}
