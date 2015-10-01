package io.crm.web.template.table;

import io.crm.web.template.TemplateUtil;
import org.watertemplate.Template;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static io.crm.util.Util.getOrDefault;
import static io.crm.web.template.TemplateUtil.EMPTY_TEMPLATE;

/**
 * Created by someone on 01/10/2015.
 */
public class TableFooter extends Template {

    TableFooter(final String id, final String classes, final List<TableRow> tableRows) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        final List<TableRow> list = tableRows == null ? Collections.EMPTY_LIST : tableRows;
        addCollection("rows", list.stream().map(r -> getOrDefault(r, EMPTY_TEMPLATE).render()).collect(Collectors.toList()));
    }

    @Override
    protected String getFilePath() {
        return "table-footer.html";
    }
}
