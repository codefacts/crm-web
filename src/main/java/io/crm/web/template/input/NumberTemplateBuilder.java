package io.crm.web.template.input;

public class NumberTemplateBuilder {
    private String id;
    private String classes;
    private String name;
    private String value;
    private String placeholder;

    public NumberTemplateBuilder setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public NumberTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public NumberTemplateBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public NumberTemplateBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public NumberTemplateBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public NumberTemplate createNumberTemplate() {
        return new NumberTemplate(id, classes, name, value, placeholder);
    }
}