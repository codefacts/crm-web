package io.crm.web;

import io.crm.web.controller.ReactJSController;

/**
 * Created by someone on 21/09/2015.
 */
public enum Uris {
    SESSION_COUNT("/session-count", "Session Count"),
    DASHBOARD("/dashboard", "Dashboard"),
    LOGIN("/login", "Login"),
    REGISTER("/register", "Register"),
    STATIC_RESOURCES_PATTERN("/static/*", "Static Resources"),
    PUBLIC_RESOURCES_PATTERN("/public/*", "Public Resources"),
    EVENT_PUBLISH_FORM("/event-publish-form", "Publish And Send Event"),
    LOGOUT("/logout", "Logout"),
    REACT_JS_TEMPLATE("/reactjs/:" + ReactJSController.TEMPLATE_NAME, "ReactJsTemplate"),
    GOOGLE_MAP("/google-map", "Google Map");

    public final String value;
    public final String label;

    Uris(final String value, String label) {
        this.value = value;
        this.label = label;
    }
}
