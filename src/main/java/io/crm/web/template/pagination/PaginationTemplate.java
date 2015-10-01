package io.crm.web.template.pagination;

import io.crm.web.util.Pagination;
import org.watertemplate.Template;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 01/10/2015.
 */
public class PaginationTemplate extends Template {
    public static final int defaultNavLength = 5;

    PaginationTemplate(final String id, final String classes, List<Template> templates) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        final List<Template> aDefault = getOrDefault(templates, Collections.<Template>emptyList());
        addCollection("lists", aDefault.stream().map(m -> m.render()).collect(Collectors.toList()));
    }

    @Override
    protected String getFilePath() {
        return "pagination.html";
    }
}
