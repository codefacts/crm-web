package io.crm.web;

/**
 * Created by someone on 21/09/2015.
 */
public enum Uris {
    home("/dashboard"),
    login("/login"),
    register("/register"),
    staticResources("/static/*"),
    publicResources("/public/*"),
    event_publish_form("/event-publish-form"),
    logout("/logout");


    public final String value;

    Uris(final String value) {
        this.value = value;
    }
}
