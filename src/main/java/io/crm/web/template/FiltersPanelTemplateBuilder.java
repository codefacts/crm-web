package io.crm.web.template;

import io.crm.intfs.ConsumerUnchecked;

final public class FiltersPanelTemplateBuilder {
    private final String title;
    private FormTemplateBuilder formTemplateBuilder = new FormTemplateBuilder();

    public FiltersPanelTemplateBuilder(String title) {
        this.title = title;
    }

    public FiltersPanelTemplateBuilder configureForm(final ConsumerUnchecked<FormTemplateBuilder> formBuilder) {
        try {
            formBuilder.accept(formTemplateBuilder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public FiltersPanelTemplate build() {
        return new FiltersPanelTemplate(title, formTemplateBuilder.build());
    }
}