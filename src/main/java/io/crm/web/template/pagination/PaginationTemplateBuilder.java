package io.crm.web.template.pagination;

import io.crm.intfs.ConsumerUnchecked;
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

    public PaginationTemplateBuilder prev(String href, final boolean hasPrev) {
        return prev(f -> f.setHref(href).addClass(!hasPrev ? "disabled" : ""));
    }

    public PaginationTemplateBuilder prev(ConsumerUnchecked<PaginationFirstLastButtonTemplateBuilder> paginationItemBuilderConsumer) {
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

    public PaginationTemplateBuilder next(String href, final boolean hasNext) {
        return next(n -> n.setHref(href).addClass(!hasNext ? "disabled" : ""));
    }

    public PaginationTemplateBuilder next(ConsumerUnchecked<PaginationFirstLastButtonTemplateBuilder> paginationItemBuilderConsumer) {
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

    public PaginationTemplateBuilder first(String href, boolean isFirst) {
        return first(f -> f.setHref(href).addClass(isFirst ? "disabled" : ""));
    }

    public PaginationTemplateBuilder first(ConsumerUnchecked<PaginationFirstLastButtonTemplateBuilder> paginationItemBuilderConsumer) {
        final PaginationFirstLastButtonTemplateBuilder first = new PaginationFirstLastButtonTemplateBuilder();
        try {
            first
                    .setArea_hidden(true)
                    .setArea_label("First")
                    .setSymbol("First")
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

    public PaginationTemplateBuilder last(String href, final boolean isLast) {
        return last(l -> l.setHref(href).addClass(isLast ? "disabled" : ""));
    }

    public PaginationTemplateBuilder last(ConsumerUnchecked<PaginationFirstLastButtonTemplateBuilder> paginationItemBuilderConsumer) {
        final PaginationFirstLastButtonTemplateBuilder last = new PaginationFirstLastButtonTemplateBuilder();
        try {
            last
                    .setArea_hidden(true)
                    .setArea_label("Last")
                    .setSymbol("Last")
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

    public PaginationTemplateBuilder addItem(ConsumerUnchecked<PaginationItemTemplateBuilder> paginationItemBuilderConsumer) {
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