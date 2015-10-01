package io.crm.web.template.pagination;

import java.util.HashSet;
import java.util.Set;

public class PaginationFirstLastButtonTemplateBuilder {
    private String id;
    private String href;
    private String area_label;
    private String area_hidden;
    private final Set<String> classesSet = new HashSet<>();
    private String symbol;

    public PaginationFirstLastButtonTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public PaginationFirstLastButtonTemplateBuilder setSymbol(String symbol) {
        this.symbol = symbol;
        return this;
    }

    public PaginationFirstLastButtonTemplateBuilder setHref(String href) {
        this.href = href;
        return this;
    }

    public PaginationFirstLastButtonTemplateBuilder setArea_label(String area_label) {
        this.area_label = area_label;
        return this;
    }

    public PaginationFirstLastButtonTemplateBuilder setArea_hidden(boolean hidden) {
        this.area_hidden = hidden ? "true" : "false";
        return this;
    }

    public PaginationFirstLastButtonTemplateBuilder addClass(final String className) {
        classesSet.add(className);
        return this;
    }

    public PaginationFirstLastButtonTemplate createPaginationFirstLastButtonTemplate() {
        return new PaginationFirstLastButtonTemplate(id, String.join(" ", classesSet), href, area_label, area_hidden, symbol);
    }
}