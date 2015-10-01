package io.crm.web.template.pagination;

import java.util.HashSet;
import java.util.Set;

public class PaginationItemTemplateBuilder {
    private String id;
    private String href;
    private String label;
    private final Set<String> classesSet = new HashSet<>();

    public PaginationItemTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public PaginationItemTemplateBuilder setHref(String href) {
        this.href = href;
        return this;
    }

    public PaginationItemTemplateBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public PaginationItemTemplateBuilder addClass(final String clasz) {
        classesSet.add(clasz);
        return this;
    }

    public PaginationItemTemplate createPaginationItemTemplate() {
        return new PaginationItemTemplate(id, String.join(" ", classesSet), href, label);
    }
}