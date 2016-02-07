package io.crm.web.template.table;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.crm.web.template.TemplateUtil.EMPTY_TEMPLATE;

/**
 * Created by someone on 01/10/2015.
 */
public class TableRow extends Template {
    TableRow(final String id, final String classes, final List<Template> tableCells) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        final List<Template> list = tableCells == null ? Collections.EMPTY_LIST : tableCells;
        addCollection("tds", list.stream().map(m -> Util.or(m, EMPTY_TEMPLATE).render()).collect(Collectors.toList()));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("table-row.html");
    }
}
