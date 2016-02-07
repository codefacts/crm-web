package io.crm.web.template.table;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

/**
 * Created by someone on 01/10/2015.
 */
public class Th extends Template {

    Th(final String id, final String classes, final String body) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        add("body", Util.or(body, ""));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("th.html");
    }
}
