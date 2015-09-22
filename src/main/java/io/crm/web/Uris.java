package io.crm.web;

/**
 * Created by someone on 21/09/2015.
 */
public enum Uris {
    dashboard("/dashboard"),
    login("/login"),
    register("/register"),
    staticResourcesPattern("/static/*"),
    publicResourcesPattern("/public/*"),
    event_publish_form("/event-publish-form"),
    logout("/logout");


    public final String value;

    Uris(final String value) {
        this.value = value;
    }
}
