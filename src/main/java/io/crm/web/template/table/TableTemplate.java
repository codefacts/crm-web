package io.crm.web.template.table;

import io.crm.util.Util;
import io.crm.web.template.Page;
import io.crm.web.template.TemplateUtil;
import org.watertemplate.Template;

import static io.crm.util.Util.*;
import static io.crm.web.template.TemplateUtil.EMPTY_TEMPLATE;

/**
 * Created by someone on 01/10/2015.
 */
public class TableTemplate extends Template {

    TableTemplate(final String id, final String classes, final Template header, final Template body, final Template footer) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        add("header", getOrDefault(header, EMPTY_TEMPLATE).render());
        add("body", getOrDefault(body, EMPTY_TEMPLATE).render());
        add("footer", getOrDefault(footer, EMPTY_TEMPLATE).render());
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("table.html");
    }
}
