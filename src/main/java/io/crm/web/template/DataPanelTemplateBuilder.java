package io.crm.web.template;

import io.vertx.core.json.JsonObject;

import java.util.List;

public class DataPanelTemplateBuilder {
    private final String title;
    private List<JsonObject> data;

    public DataPanelTemplateBuilder(String title) {
        this.title = title;
    }

    public DataPanelTemplateBuilder setData(List<JsonObject> data) {
        this.data = data;
        return this;
    }

    public DataPanelTemplate build() {
        return new DataPanelTemplate(title, data);
    }
}