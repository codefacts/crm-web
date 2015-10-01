package io.crm.web.css.bootstrap;

/**
 * Created by someone on 01/10/2015.
 */
public enum TableClasses {
    CONDENSED("table-condensed"),
    HOVER("table-hover"),
    STRIPED("table-striped"),
    BORDERED("table-bordered");
    
    public final String value;

    TableClasses(String value) {
        this.value = value;
    }
}
