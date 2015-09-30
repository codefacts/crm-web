package io.crm.web.template;

import org.watertemplate.Template;

public class ColumnTemplateBuilder {
    private Template content;
    private String classes;
    private String id;

    public ColumnTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public ColumnTemplateBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public ColumnTemplateBuilder setContent(Template content) {
        this.content = content;
        return this;
    }

    public ColumnTemplate createColumnTemplate() {
        return new ColumnTemplate(id, classes, content);
    }
}