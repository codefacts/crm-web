package io.crm.web.template.input;

public class RangeTemplateBuilder {
    private String id;
    private String classes;
    private String name;
    private Number from;
    private Number to;
    private String placeholderFrom;
    private String placeholderTo;

    public RangeTemplateBuilder setPlaceholderTo(String placeholderTo) {
        this.placeholderTo = placeholderTo;
        return this;
    }

    public RangeTemplateBuilder setPlaceholderFrom(String placeholderFrom) {
        this.placeholderFrom = placeholderFrom;
        return this;
    }

    public RangeTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public RangeTemplateBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public RangeTemplateBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RangeTemplateBuilder setFrom(Number from) {
        this.from = from;
        return this;
    }

    public RangeTemplateBuilder setTo(Number to) {
        this.to = to;
        return this;
    }

    public RangeTemplate createRangeTemplate() {
        return new RangeTemplate(id, classes, name, from, to, placeholderFrom, placeholderTo);
    }
}