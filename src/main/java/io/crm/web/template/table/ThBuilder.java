package io.crm.web.template.table;

import org.watertemplate.Template;

public class ThBuilder {
    private String id;
    private String classes;
    private String body;

    public ThBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public ThBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public ThBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public Th createTh() {
        return new Th(id, classes, body);
    }
}