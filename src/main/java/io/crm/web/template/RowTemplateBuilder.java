package io.crm.web.template;

import org.watertemplate.Template;

import java.util.Collection;

public class RowTemplateBuilder {
    private String id;
    private String content;
    private String name;
    private String classes;

    public RowTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public RowTemplateBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public RowTemplateBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RowTemplateBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public RowTemplate createRowTemplate() {
        return new RowTemplate(id, classes, name, content);
    }
}