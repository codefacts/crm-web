package io.crm.web.template.input;

public class TextInputTemplateBuilder {
    private String id;
    private String classes;
    private String name;
    private String value;
    private String placeholder;

    public TextInputTemplateBuilder setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }
    public TextInputTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public TextInputTemplateBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public TextInputTemplateBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public TextInputTemplateBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public TextInputTemplate createTextInputTemplate() {
        return new TextInputTemplate(id, classes, name, value, placeholder);
    }
}