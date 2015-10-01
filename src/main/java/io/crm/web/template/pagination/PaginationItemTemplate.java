package io.crm.web.template.pagination;

import org.watertemplate.Template;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 01/10/2015.
 */
public class PaginationItemTemplate extends Template {

    PaginationItemTemplate(final String id, final String classes, String href, String label) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        add("href", getOrDefault(href, ""));
        add("label", getOrDefault(label, ""));
    }

    @Override
    protected String getFilePath() {
        return "pagination-item.html";
    }
}
