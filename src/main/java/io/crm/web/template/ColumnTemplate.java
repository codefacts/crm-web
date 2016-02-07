package io.crm.web.template;

import io.crm.util.Util;
import org.watertemplate.Template;

/**
 * Created by someone on 30/09/2015.
 */
public class ColumnTemplate extends Template {

    public ColumnTemplate(String id, String classes, Template template) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, "col-md-2"));
        add("body", template.render());
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("column.html");
    }
}
