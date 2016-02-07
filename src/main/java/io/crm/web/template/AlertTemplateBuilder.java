package io.crm.web.template;

import io.crm.util.Util;

import java.util.HashSet;
import java.util.Set;

public class AlertTemplateBuilder {
    private String id;
    private final Set<String> classesSet = new HashSet<>();
    private String body;

    public AlertTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public AlertTemplateBuilder success(String body) {
        classesSet.add("alert-success");
        this.body = Util.or(body, "");
        return this;
    }

    public AlertTemplateBuilder info(String body) {
        classesSet.add("alert-info");
        this.body = Util.or(body, "");
        return this;
    }

    public AlertTemplateBuilder warning(String body) {
        classesSet.add("alert-warning");
        this.body = Util.or(body, "");
        return this;
    }

    public AlertTemplateBuilder danger(String body) {
        classesSet.add("alert-danger");
        this.body = Util.or(body, "");
        return this;
    }

    public AlertTemplateBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public AlertTemplate createAlertTemplate() {
        return new AlertTemplate(id, String.join(" ", classesSet), body);
    }
}