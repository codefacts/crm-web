package io.crm.web.template.form;

import io.crm.web.css.WebButtonClasses;
import io.crm.web.template.input.ButtonTemplateBuilder;
import org.watertemplate.Template;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by someone on 01/10/2015.
 */
public final class DefaultFooterBuilder {
    private final List<Template> templates = new ArrayList<>();

    public DefaultFooterBuilder search() {
        templates.add(
                new ButtonTemplateBuilder()
                        .primary()
                        .setBtnText("Search")
                        .pullRight()
                        .addClass(WebButtonClasses.BTN_FOOTER.value)
                        .createButtonTemplate()
        );
        return this;
    }

    public DefaultFooterBuilder clear() {
        templates.add(
                new ButtonTemplateBuilder()
                        .danger()
                        .setBtnText("Clear")
                        .pullRight()
                        .addClass(WebButtonClasses.BTN_FOOTER.value)
                        .createButtonTemplate()
        );
        return this;
    }

    public DefaultFooterBuilder export() {
        templates.add(
                new ButtonTemplateBuilder()
                        .success()
                        .setBtnText("Export")
                        .pullRight()
                        .addClass(WebButtonClasses.BTN_FOOTER.value)
                        .createButtonTemplate()
        );
        return this;
    }

    public DefaultFooterBuilder addTemplate(final Template template) {
        templates.add(template);
        return this;
    }

    public List<Template> build() {
        return templates;
    }
}
