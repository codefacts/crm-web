package io.crm.web.template;

import io.crm.web.template.table.TableTemplate;
import org.watertemplate.Template;

import static io.crm.util.Util.getOrDefault;
import static io.crm.web.template.TemplateUtil.EMPTY_TEMPLATE;

/**
 * Created by someone on 23/09/2015.
 */
public class DataPanelTemplate extends Template {

    public DataPanelTemplate(final String title, final TableTemplate tableTemplate, final Template paginationTemplate) {
        add("title", getOrDefault(title, ""));
        add("table", getOrDefault(tableTemplate, EMPTY_TEMPLATE).render());
        add("pagination", getOrDefault(paginationTemplate, EMPTY_TEMPLATE).render());
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("data-panel.html");
    }
}
