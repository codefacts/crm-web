package io.crm.web.template.input;

import java.util.Date;

public class DateInputTemplateBuilder {
    private String id;
    private String classes;
    private String name;
    private Date value;
    private String placeholder;

    public DateInputTemplateBuilder setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public DateInputTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public DateInputTemplateBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public DateInputTemplateBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DateInputTemplateBuilder setValue(Date value) {
        this.value = value;
        return this;
    }

    public DateInputTemplate createDateInputTemplate() {
        return new DateInputTemplate(id, classes, name, value, placeholder);
    }
}