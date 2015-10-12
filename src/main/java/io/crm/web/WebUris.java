package io.crm.web;

/**
 * Created by someone on 21/09/2015.
 */
public enum WebUris {
    fileUpload("/file-upload", "Upload File"),
    dashboard("/dashboard", "Dashboard"),
    login("/login", "Login"),
    register("/register", "Register"),
    staticResourcesPattern("/static/*", "Static Resources"),
    publicResourcesPattern("/public/*", "Public Resources"),
    event_publish_form("/event-publish-form", "Publish And Send Event"),
    logout("/logout", "Logout"),
    callDetails("/call/details", "Call Details"),
    br_checker_details("/br-checker/details", "Br Checker Details"),
    br_checker_view("/br-details/view", "Br Checker Info"),
    BrCheckerImages("/br-checker/images", "Br Checker Images"), googleMap("/google-map", "Google Map");


    public final String value;
    public final String label;

    WebUris(final String value, String label) {
        this.value = value;
        this.label = label;
    }
}
