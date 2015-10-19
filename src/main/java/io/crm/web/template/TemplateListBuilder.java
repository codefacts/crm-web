package io.crm.web.template;

import org.watertemplate.Template;

import java.util.Collection;

public class TemplateListBuilder {
    private Collection<Template> templates;

    public TemplateListBuilder setTemplates(final Collection<Template> templates) {
        this.templates = templates;
        return this;
    }

    public TemplateList createTemplateList() {
        return new TemplateList(templates);
    }
}