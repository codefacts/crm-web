package io.crm.web.template;

import io.crm.web.template.model.Header;
import io.crm.web.template.table.TableTemplate;
import org.watertemplate.Template;

import java.util.List;

/**
 * Created by someone on 23/09/2015.
 */
public class DataPanelTemplate extends Template {

    public DataPanelTemplate(final String title, final TableTemplate tableTemplate, final List<Header> headers, final Template paginationTemplate) {
        add("title", title);
        add("table", tableTemplate.render());
        add("pagination", paginationTemplate.render());
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("data-panel.html");
    }
}
