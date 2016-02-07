package io.crm.web.template.pagination;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by someone on 01/10/2015.
 */
public class PaginationTemplate extends Template {
    public static final int defaultNavLength = 5;

    PaginationTemplate(final String id, final String classes, List<Template> templates) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        final List<Template> aDefault = Util.or(templates, Collections.<Template>emptyList());
        addCollection("lists", aDefault.stream().map(m -> m.render()).collect(Collectors.toList()));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("pagination.html");
    }
}
