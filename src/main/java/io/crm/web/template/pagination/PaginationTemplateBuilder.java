package io.crm.web.template.pagination;

import io.crm.intfs.ConsumerInterface;
import io.crm.web.util.Pagination;
import org.watertemplate.Template;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class PaginationTemplateBuilder {
    private String id;
    private final Set<String> classesSet = new HashSet<>();
    private List<Template> templates = new ArrayList<>();

    public PaginationTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public PaginationTemplateBuilder prev(String href) {
        return prev(f -> {
            f.setHref(href);
        });
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

    public PaginationTemplateBuilder next(String href) {
        return next(n -> n.setHref(href));
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

    public PaginationTemplateBuilder first(String href) {
        return first(f -> f.setHref(href));
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

    public PaginationTemplateBuilder last(String href) {
        return last(l -> l.setHref(href));
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

    public PaginationTemplateBuilder addAllItems(Consumer<List<PaginationItemTemplate>> consumer) {
        ArrayList<PaginationItemTemplate> paginationItemTemplates = new ArrayList<>();
        consumer.accept(paginationItemTemplates);
        templates.addAll(paginationItemTemplates);
        return this;
    }

    public PaginationTemplate createPaginationTemplate() {
        return new PaginationTemplate(id, String.join(" ", classesSet), templates);
    }

    public PaginationTemplateBuilder addClass(final String clasz) {
        classesSet.add(clasz);
        return this;
    }
}