package io.crm.web.template;

import io.crm.util.Util;
import io.crm.web.template.table.TableTemplate;
import org.watertemplate.Template;

import static io.crm.web.template.TemplateUtil.EMPTY_TEMPLATE;

/**
 * Created by someone on 23/09/2015.
 */
public class DataPanelTemplate extends Template {

    public DataPanelTemplate(final String title, final TableTemplate tableTemplate, final Template paginationTemplate, final String exportButton) {
        add("title", Util.or(title, ""));
        add("table", Util.or(tableTemplate, EMPTY_TEMPLATE).render());
        add("pagination", Util.or(paginationTemplate, EMPTY_TEMPLATE).render());
        add("exportButton", Util.or(exportButton, ""));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("data-panel.html");
    }
}
