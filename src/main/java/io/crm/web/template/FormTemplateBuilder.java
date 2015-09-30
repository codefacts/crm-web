package io.crm.web.template;

import io.crm.intfs.ConsumerInterface;
import io.crm.web.template.form.FormRowBuilder;
import org.watertemplate.Template;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.crm.util.Util.getOrDefault;
import static java.util.Collections.EMPTY_SET;

/**
 * Created by someone on 30/09/2015.
 */
final public class FormTemplateBuilder {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");
    private String id;
    private Set<String> classes;
    private List<Template> templates = new ArrayList<>();

    public FormTemplateBuilder id(final String id) {
        this.id = id;
        return this;
    }

    public FormTemplateBuilder addClass(final String clasz) {
        classes.add(clasz);
        return this;
    }

    public FormTemplateBuilder addRow(final ConsumerInterface<FormRowBuilder> consumer) {
        final FormRowBuilder rowBuilder = new FormRowBuilder();
        try {
            consumer.accept(rowBuilder);
            templates.add(rowBuilder.build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public FormTemplateBuilder add(final Template template) {
        templates.add(template);
        return this;
    }

    public FormTemplate build() {
        return new FormTemplate(id, String.join(" ", getOrDefault(classes, EMPTY_SET)), getOrDefault(templates, EMPTY_SET));
    }

}
