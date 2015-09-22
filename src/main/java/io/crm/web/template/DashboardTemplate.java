package io.crm.web.template;

import io.crm.QC;
import io.vertx.core.json.JsonObject;
import org.watertemplate.Template;
import org.watertemplate.TemplateMap;

/**
 * Created by someone on 22/09/2015.
 */
public class DashboardTemplate extends Template {
    private final JsonObject user;
    private final Template sidebarTemplate;
    private final Template contentTemplate;

    public DashboardTemplate(JsonObject user, Template sidebarTemplate, Template contentTemplate) {
        this.user = user;
        this.sidebarTemplate = sidebarTemplate;
        this.contentTemplate = contentTemplate;
        add(QC.username, user.getString(QC.username));
    }

    @Override
    protected void addSubTemplates(TemplateMap.SubTemplates subTemplates) {
        subTemplates.add("sidebar", sidebarTemplate);
        subTemplates.add("main_content", contentTemplate);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("dashboard.html");
    }
}
