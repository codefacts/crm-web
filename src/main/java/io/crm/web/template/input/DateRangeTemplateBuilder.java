package io.crm.web.template.input;

import java.util.Date;

public class DateRangeTemplateBuilder {
    private String id;
    private String classes;
    private String name;
    private Date from;
    private Date to;
    private String placeholderFrom;
    private String placeholderTo;

    public DateRangeTemplateBuilder setPlaceholderFrom(String placeholderFrom) {
        this.placeholderFrom = placeholderFrom;
        return this;
    }

    public DateRangeTemplateBuilder setPlaceholderTo(String placeholderTo) {
        this.placeholderTo = placeholderTo;
        return this;
    }

    public DateRangeTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public DateRangeTemplateBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public DateRangeTemplateBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DateRangeTemplateBuilder setFrom(Date from) {
        this.from = from;
        return this;
    }

    public DateRangeTemplateBuilder setTo(Date to) {
        this.to = to;
        return this;
    }

    public DateRangeTemplate createDateRangeTemplate() {
        return new DateRangeTemplate(id, classes, name, from, to, placeholderFrom, placeholderTo);
    }
}