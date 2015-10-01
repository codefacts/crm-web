package io.crm.web.css.bootstrap;

/**
 * Created by someone on 01/10/2015.
 */
public enum ButtonClasses {
    BTN_SUCCESS("btn-success"), BTN_PRIMARY("btn-primary"), BTN_DANGER("btn-danger"), BTN_WARNING("btn-warning"), BTN_DEFAULT("btn-default"), BTN_INFO("btn-info"), BTN_LINK("btn-link");

    public final String value;

    ButtonClasses(String value) {
        this.value = value;
    }
}
