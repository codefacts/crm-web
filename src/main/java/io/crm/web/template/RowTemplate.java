package io.crm.web.template;

import io.crm.util.Util;
import org.watertemplate.Template;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 30/09/2015.
 */
public class RowTemplate extends Template {

    public RowTemplate(String id, String classes, String name, String content) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        add("name", getOrDefault(name, ""));
        add("body", getOrDefault(content, ""));
    }

    @Override
    protected String getFilePath() {
        return "row.html";
    }
}
