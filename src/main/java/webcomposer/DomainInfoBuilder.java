package webcomposer;

public class DomainInfoBuilder {
    private String name;
    private String plural = null;
    private String label = null;
    private String labelPlural = null;
    private String uri = null;
    private String address = null;
    private String table = null;

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

    public DomainInfoBuilder setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public DomainInfoBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public DomainInfoBuilder setTable(String table) {
        this.table = table;
        return this;
    }

    public DomainInfo createDomainInfo() {
        return new DomainInfo(name, plural, label, labelPlural, uri, address, table);
    }
}