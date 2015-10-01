package io.crm.web.template.pagination;

import io.crm.intfs.ConsumerInterface;
import io.crm.web.util.Pagination;
import org.watertemplate.Template;

import java.util.ArrayList;
import java.util.List;

public class PaginationTemplateBuilder {
    private String id;
    private String classes;
    private List<Template> templates = new ArrayList<>();

    public PaginationTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public PaginationTemplateBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public PaginationTemplateBuilder prev(ConsumerInterface<PaginationFirstLastButtonTemplateBuilder> paginationItemBuilderConsumer) {
        final PaginationFirstLastButtonTemplateBuilder prev = new PaginationFirstLastButtonTemplateBuilder();
        try {
            prev
                    .setArea_hidden(true)
                    .setArea_label("Previous")
                    .setSymbol("&laquo;")
            ;
            paginationItemBuilderConsumer.accept(prev);
            templates.add(prev.createPaginationFirstLastButtonTemplate());
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            throw new RuntimeException(e);
        }
        return this;
    }

    public PaginationTemplateBuilder next(ConsumerInterface<PaginationFirstLastButtonTemplateBuilder> paginationItemBuilderConsumer) {
        final PaginationFirstLastButtonTemplateBuilder next = new PaginationFirstLastButtonTemplateBuilder();
        try {
            next
                    .setArea_hidden(true)
                    .setArea_label("Next")
                    .setSymbol("&raquo;")
            ;
            paginationItemBuilderConsumer.accept(next);
            templates.add(next.createPaginationFirstLastButtonTemplate());
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            throw new RuntimeException(e);
        }
        return this;
    }

    public PaginationTemplateBuilder first(ConsumerInterface<PaginationFirstLastButtonTemplateBuilder> paginationItemBuilderConsumer) {
        final PaginationFirstLastButtonTemplateBuilder first = new PaginationFirstLastButtonTemplateBuilder();
        try {
            first
                    .setArea_hidden(true)
                    .setArea_label("First")
            ;
            paginationItemBuilderConsumer.accept(first);
            templates.add(first.createPaginationFirstLastButtonTemplate());
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            throw new RuntimeException(e);
        }
        return this;
    }

    public PaginationTemplateBuilder last(ConsumerInterface<PaginationFirstLastButtonTemplateBuilder> paginationItemBuilderConsumer) {
        final PaginationFirstLastButtonTemplateBuilder last = new PaginationFirstLastButtonTemplateBuilder();
        try {
            last
                    .setArea_hidden(true)
                    .setArea_label("Last")
            ;
            paginationItemBuilderConsumer.accept(last);
            templates.add(last.createPaginationFirstLastButtonTemplate());
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            throw new RuntimeException(e);
        }
        return this;
    }

    public PaginationTemplateBuilder addItem(ConsumerInterface<PaginationItemTemplateBuilder> paginationItemBuilderConsumer) {
        final PaginationItemTemplateBuilder prev = new PaginationItemTemplateBuilder();
        try {
            paginationItemBuilderConsumer.accept(prev);
            templates.add(prev.createPaginationItemTemplate());
        } catch (Exception e) {
            if (e instanceof RuntimeException)
                throw (RuntimeException) e;
            throw new RuntimeException(e);
        }
        return this;
    }

    public PaginationTemplateBuilder addAllItems(List<PaginationItemTemplate> paginationItemTemplates) {
        templates.addAll(paginationItemTemplates);
        return this;
    }

    public PaginationTemplate createPaginationTemplate() {
        return new PaginationTemplate(id, classes, templates);
    }
}