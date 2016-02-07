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
public class TableBody extends Template {

    TableBody(final String id, final String classes, final List<TableRow> tableRows) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        final List<TableRow> list = tableRows == null ? Collections.EMPTY_LIST : tableRows;
        addCollection("rows", list.stream().map(r -> Util.or(r, EMPTY_TEMPLATE).render()).collect(Collectors.toList()));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("table-body.html");
    }

    public static void main(String... args) {
        System.out.println(

        );
    }
}
