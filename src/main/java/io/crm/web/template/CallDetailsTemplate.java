package io.crm.web.template;

import org.watertemplate.Template;
import org.watertemplate.TemplateMap;

/**
 * Created by someone on 23/09/2015.
 */
public class CallDetailsTemplate extends Template {
    private final Template filtersPanel;
    private final Template dataPanel;

    public CallDetailsTemplate(Template filtersPanel, Template dataPanel) {
        this.filtersPanel = filtersPanel;
        this.dataPanel = dataPanel;
    }

    @Override
    protected void addSubTemplates(TemplateMap.SubTemplates subTemplates) {
        subTemplates.add("filtersPanel", filtersPanel);
        subTemplates.add("dataPanel", dataPanel);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("call-details.html");
    }
}
