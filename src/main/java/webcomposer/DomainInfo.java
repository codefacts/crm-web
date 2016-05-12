package webcomposer;

import io.crm.web.util.WebUtils;

import java.util.Objects;

/**
 * Created by shahadat on 5/3/16.
 */
public class DomainInfo {
    public final String name;
    public final String plural;
    public final String label;
    public final String labelPlural;
    public final String uri;
    public final String address;

    public DomainInfo(String name) {
        this(name, null, null, null, null, null);
    }

    public DomainInfo(String name, String plural) {
        this(name == null ? plural.substring(0, plural.length() - 1) : name, plural, null, null, null, null);
    }

    public DomainInfo(String name, String plural, String label, String labelPlural, String uri, String address) {
        Objects.requireNonNull(name);
        this.name = name;
        this.plural = plural == null ? this.name + "s" : plural;
        this.label = (label == null) ? WebUtils.toTitle(this.name) : label;
        this.labelPlural = labelPlural == null ? WebUtils.toTitle(this.plural) : labelPlural;

        //Init
        this.uri = uri == null ? "/" + this.labelPlural.toLowerCase().replace(' ', '-') : uri;
        this.address = address == null ? this.labelPlural.toLowerCase().replace(' ', '-') : address;
    }

    public static DomainInfo create(String name, String plural) {
        return new DomainInfoBuilder().setName(name).setPlural(plural).createDomainInfo();
    }


}
