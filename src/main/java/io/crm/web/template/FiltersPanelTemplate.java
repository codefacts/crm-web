package io.crm.web.template;

import org.watertemplate.Template;
import org.watertemplate.TemplateMap;

/**
 * Created by someone on 23/09/2015.
 */
public class FiltersPanelTemplate extends Template {
    private final FormTemplate formTemplate;

    public FiltersPanelTemplate(final String title, FormTemplate formTemplate) {
        this.formTemplate = formTemplate;
        add("title", title);
    }

    @Override
    protected void addSubTemplates(TemplateMap.SubTemplates subTemplates) {
        subTemplates.add("form", formTemplate);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("filters-panel.html");
    }
}
