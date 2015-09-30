package io.crm.web.template.form;

import io.crm.web.template.FormTemplateBuilder;

/**
 * Created by someone on 30/09/2015.
 */
final public class RangeInputBuilder<T> {
    private String id;
    private String classes;
    private String name;
    private T from;
    private T to;
    private String columnClasses;
    private String placeholderFrom;
    private String placeholderTo;

    public String getPlaceholderFrom() {
        return placeholderFrom;
    }

    public RangeInputBuilder<T> setPlaceholderFrom(String placeholderFrom) {
        this.placeholderFrom = placeholderFrom;
        return this;
    }

    public RangeInputBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public RangeInputBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public RangeInputBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RangeInputBuilder setFrom(T from) {
        this.from = from;
        return this;
    }

    public RangeInputBuilder setTo(T to) {
        this.to = to;
        return this;
    }

    public RangeInputBuilder setColumnClasses(String columnClasses) {
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

    public T getFrom() {
        return from;
    }

    public T getTo() {
        return to;
    }

    public String getColumnClasses() {
        return columnClasses;
    }

    public String getPlaceholderTo() {
        return placeholderTo;
    }

    public RangeInputBuilder<T> setPlaceholderTo(String placeholderTo) {
        this.placeholderTo = placeholderTo;
        return this;
    }
}
