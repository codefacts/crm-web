package io.crm.web.template.table;

import org.watertemplate.Template;

import java.util.HashSet;
import java.util.Set;

public class TableTemplateBuilder {
    private String id;
    private final Set<String> classesSet = new HashSet<>();
    private Template header;
    private Template body;
    private Template footer;

    public TableTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public TableTemplateBuilder addClass(String clasz) {
        classesSet.add(clasz);
        return this;
    }

    public TableTemplateBuilder setHeader(Template header) {
        this.header = header;
        return this;
    }

    public TableTemplateBuilder setBody(Template body) {
        this.body = body;
        return this;
    }

    public TableTemplateBuilder setFooter(Template footer) {
        this.footer = footer;
        return this;
    }

    public TableTemplate createTableTemplate() {
        return new TableTemplate(id, String.join(" ", classesSet), header, body, footer);
    }
}