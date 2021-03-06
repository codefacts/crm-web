package io.crm.web.template;

import io.crm.intfs.ConsumerUnchecked;
import io.crm.util.Util;
import io.crm.web.template.form.DefaultFooterBuilder;
import io.crm.web.template.form.FormRowBuilder;
import org.watertemplate.Template;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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

    public FormTemplateBuilder addRow(final ConsumerUnchecked<FormRowBuilder> consumer) {
        final FormRowBuilder rowBuilder = new FormRowBuilder();
        try {
            consumer.accept(rowBuilder);
            templates.add(rowBuilder.build());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    public FormTemplateBuilder addTemplate(final Template template) {
        templates.add(template);
        return this;
    }

    public FormTemplateBuilder defaultFooter() {
        templates.addAll(
                new DefaultFooterBuilder()
                        .search()
                        .clear()
                        .export()
                        .build()
        );
        return this;
    }

    public FormTemplateBuilder defaultFooter(final ConsumerUnchecked<DefaultFooterBuilder> builderConsumer) {
        final DefaultFooterBuilder defaultFooterBuilder = new DefaultFooterBuilder();
        try {
            builderConsumer.accept(defaultFooterBuilder);
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        }
        addTemplate(defaultFooterBuilder.build());
        return this;
    }

    private void addTemplate(final List<Template> templates) {
        templates.addAll(templates);
    }

    public FormTemplate build() {
        return new FormTemplate(id, String.join(" ", Util.or(classes, EMPTY_SET)), Util.or(templates, EMPTY_SET));
    }

}
