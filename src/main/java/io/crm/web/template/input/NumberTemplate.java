package io.crm.web.template.input;

import org.watertemplate.Template;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 30/09/2015.
 */
public class NumberTemplate extends Template {

    public NumberTemplate(String id, String classes, String name, String value, String placeholder) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        add("name", getOrDefault(name, ""));
        add("value", getOrDefault(value, ""));
        add("placeholder", getOrDefault(placeholder, ""));
    }

    @Override
    protected String getFilePath() {
        return "number.html";
    }
}
