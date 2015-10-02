package io.crm.web.template;

public class SidebarTemplateBuilder {
    private String currentUri;

    public SidebarTemplateBuilder setCurrentUri(String currentUri) {
        this.currentUri = currentUri;
        return this;
    }

    public SidebarTemplate createSidebarTemplate() {
        return new SidebarTemplate(currentUri);
    }
}