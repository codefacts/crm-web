package io.crm.web.template;

import io.crm.util.Util;
import org.watertemplate.Template;

import java.util.Collections;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 30/09/2015.
 */
public class ColumnTemplate extends Template {

    public ColumnTemplate(String id, String classes, Template template) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, "col-md-2"));
        add("body", template.render());
    }

    @Override
    protected String getFilePath() {
        return "column.html";
    }
}
