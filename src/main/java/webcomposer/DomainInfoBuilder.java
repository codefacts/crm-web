package webcomposer;

public class DomainInfoBuilder {
    private String name;
    private String plural = null;
    private String label = null;
    private String labelPlural = null;

    public DomainInfoBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public DomainInfoBuilder setPlural(String plural) {
        this.plural = plural;
        return this;
    }

    public DomainInfoBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public DomainInfoBuilder setLabelPlural(String labelPlural) {
        this.labelPlural = labelPlural;
        return this;
    }

    public DomainInfo createDomainInfo() {
        return new DomainInfo(name, plural, label, labelPlural);
    }
}