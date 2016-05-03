package webcomposer;

import io.crm.util.Util;
import io.crm.web.util.WebUtils;

/**
 * Created by shahadat on 5/3/16.
 */
public class DomainInfo {
    public final String name;
    public final String plural;
    public final String label;
    public final String labelPlural;

    public DomainInfo(String name) {
        this(name, null, null, null);
    }

    public DomainInfo(String name, String plural) {
        this(name, plural, null, null);
    }

    public DomainInfo(String name, String plural, String label, String labelPlural) {
        this.name = name;
        this.plural = plural == null ? name + "s" : plural;
        this.label = (label == null) ? WebUtils.toTitle(name) : label;
        this.labelPlural = labelPlural == null ? WebUtils.toTitle(plural) : labelPlural;
    }
}
