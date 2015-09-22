package io.crm.web.template;

import org.watertemplate.Template;

public class CallDetailsTemplateBuilder {
    private Template filtersPanel;
    private Template dataPanel;

    public CallDetailsTemplateBuilder setFiltersPanel(Template filtersPanel) {
        this.filtersPanel = filtersPanel;
        return this;
    }

    public CallDetailsTemplateBuilder setDataPanel(Template dataPanel) {
        this.dataPanel = dataPanel;
        return this;
    }

    public CallDetailsTemplate build() {
        return new CallDetailsTemplate(filtersPanel, dataPanel);
    }
}