package io.crm.web.template.pagination;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

/**
 * Created by someone on 01/10/2015.
 */
public class PaginationItemTemplate extends Template {

    PaginationItemTemplate(final String id, final String classes, String href, String label) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        add("href", Util.or(href, ""));
        add("label", Util.or(label, ""));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("pagination-item.html");
    }
}
