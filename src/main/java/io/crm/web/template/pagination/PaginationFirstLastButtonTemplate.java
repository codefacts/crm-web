package io.crm.web.template.pagination;

import org.watertemplate.Template;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 01/10/2015.
 */
public class PaginationFirstLastButtonTemplate extends Template {

    PaginationFirstLastButtonTemplate(final String id, final String classes, final String href, final String area_label, String area_hidden, String symbol) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        add("href", href);
        add("area_label", area_label);
        add("area_hidden", area_hidden);
        add("symbol", symbol);
    }

    @Override
    protected String getFilePath() {
        return "pagination-first-last-button.html";
    }
}
