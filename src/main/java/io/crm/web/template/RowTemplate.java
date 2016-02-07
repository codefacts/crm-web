package io.crm.web.template;

import io.crm.util.Util;
import org.watertemplate.Template;

/**
 * Created by someone on 30/09/2015.
 */
public class RowTemplate extends Template {

    public RowTemplate(String id, String classes, String name, String content) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        add("name", Util.or(name, ""));
        add("body", Util.or(content, ""));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("row.html");
    }
}
