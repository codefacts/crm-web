package io.crm.web.template;

import io.crm.util.Util;
import io.vertx.core.json.JsonObject;
import org.watertemplate.Template;

import static io.crm.util.Util.getOrDefault;
import static io.crm.web.template.TemplateUtil.EMPTY_TEMPLATE;

public class DashboardTemplateBuilder {
    private JsonObject user;
    private Template sidebarTemplate;
    private Template contentTemplate;

    public DashboardTemplateBuilder setUser(JsonObject user) {
        this.user = user;
        return this;
    }

    public DashboardTemplateBuilder setSidebarTemplate(Template sidebarTemplate) {
        this.sidebarTemplate = sidebarTemplate;
        return this;
    }

    public DashboardTemplateBuilder setContentTemplate(Template contentTemplate) {
        this.contentTemplate = contentTemplate;
        return this;
    }

    public DashboardTemplate build() {
        return new DashboardTemplate(user, getOrDefault(sidebarTemplate, EMPTY_TEMPLATE), getOrDefault(contentTemplate, EMPTY_TEMPLATE));
    }
}