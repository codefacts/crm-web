package io.crm.web.template;

public class FiltersPanelTemplateBuilder {
    private final String title;

    public FiltersPanelTemplateBuilder(String title) {
        this.title = title;
    }

    public FiltersPanelTemplate build() {
        return new FiltersPanelTemplate(title);
    }
}