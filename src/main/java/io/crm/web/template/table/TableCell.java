package io.crm.web.template.table;

import io.crm.web.template.TemplateUtil;
import org.watertemplate.Template;

import static io.crm.util.Util.getOrDefault;
import static io.crm.web.template.TemplateUtil.EMPTY_TEMPLATE;

/**
 * Created by someone on 01/10/2015.
 */
public class TableCell extends Template {
    TableCell(final String id, final String classes, final String body) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        add("body", getOrDefault(body, ""));
    }

    @Override
    protected String getFilePath() {
        return "table-cell.html";
    }
}
