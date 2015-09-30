package io.crm.web.template.form;

import io.crm.web.template.FormTemplateBuilder;

/**
 * Created by someone on 30/09/2015.
 */
final public class InputBuilder<T> {
    private String id;
    private String classes;
    private String name;
    private T value;
    private String columnClasses = "col-md-2";
    private String placeholder;

    public InputBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public InputBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public InputBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public InputBuilder setValue(T value) {
        this.value = value;
        return this;
    }

    public InputBuilder setColumnClasses(String columnClasses) {
        this.columnClasses = columnClasses;
        return this;
    }

    public String getId() {
        return id;
    }

    public String getClasses() {
        return classes;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    public String getColumnClasses() {
        return columnClasses;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public InputBuilder<T> setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        return this;
    }
}
