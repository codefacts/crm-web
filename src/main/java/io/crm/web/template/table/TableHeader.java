package io.crm.web.template.table;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static io.crm.web.template.TemplateUtil.EMPTY_TEMPLATE;

/**
 * Created by someone on 01/10/2015.
 */
public class TableHeader extends Template {

    TableHeader(final String id, final String classes, final Collection<TableRow> tableRows) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        final Collection<TableRow> list = tableRows == null ? Collections.EMPTY_LIST : tableRows;
        addCollection("rows", list.stream().map(r -> Util.or(r, EMPTY_TEMPLATE).render()).collect(Collectors.toList()));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("table-header.html");
    }
}
