package io.crm.web.template.pagination;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

/**
 * Created by someone on 01/10/2015.
 */
public class PaginationFirstLastButtonTemplate extends Template {

    PaginationFirstLastButtonTemplate(final String id, final String classes, final String href, final String area_label, String area_hidden, String symbol) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        add("href", href);
        add("area_label", area_label);
        add("area_hidden", area_hidden);
        add("symbol", symbol);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("pagination-first-last-button.html");
    }
}
