package io.crm.web.template.input;

import io.crm.util.Util;
import org.watertemplate.Template;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 30/09/2015.
 */
public class ButtonTemplate extends Template {
    ButtonTemplate(final String id, final String classes, final String btnText) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        add("buttonText", getOrDefault(btnText, ""));
    }

    @Override
    protected String getFilePath() {
        return "button.html";
    }
}
