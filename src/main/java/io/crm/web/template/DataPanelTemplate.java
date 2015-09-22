package io.crm.web.template;

import io.vertx.core.json.JsonObject;
import org.watertemplate.Template;

import java.util.List;

/**
 * Created by someone on 23/09/2015.
 */
public class DataPanelTemplate extends Template {
    private final List<JsonObject> data;

    public DataPanelTemplate(final String title, List<JsonObject> data) {
        add("title", title);
        this.data = data;
    }

    @Override

    protected String getFilePath() {
        return Page.templatePath("data-panel.html");
    }
}
