package io.crm.web.css.bootstrap;

/**
 * Created by someone on 01/10/2015.
 */
public enum BootstrapCss {
    ACTIVE("active"),
    DISABLED("disabled"),
    PULL_LEFT("pull-left"),
    PULL_RIGHT("pull-right");

    public final String value;

    BootstrapCss(String value) {
        this.value = value;
    }
}
