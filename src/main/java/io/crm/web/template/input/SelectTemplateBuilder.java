package io.crm.web.template.input;

import java.util.Map;

public class SelectTemplateBuilder {
    private String id;
    private String classes;
    private String name;
    private Map<String, String> valuesMap;

    public SelectTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public SelectTemplateBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public SelectTemplateBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public SelectTemplateBuilder setValuesMap(Map<String, String> valuesMap) {
        this.valuesMap = valuesMap;
        return this;
    }

    public SelectTemplate createSelectTemplate() {
        return new SelectTemplate(id, classes, name, valuesMap);
    }
}