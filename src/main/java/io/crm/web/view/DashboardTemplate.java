package io.crm.web.view;

import io.crm.QC;
import io.vertx.core.json.JsonObject;
import org.watertemplate.Template;

/**
 * Created by someone on 22/09/2015.
 */
public class DashboardTemplate extends Template {
    private final JsonObject user;

    public DashboardTemplate(JsonObject user) {
        this.user = user;
        add(QC.username, user.getString(QC.username));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("dashboard.html");
    }
}
