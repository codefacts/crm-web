package io.crm.web;

/**
 * Created by someone on 21/09/2015.
 */
public enum Uris {
    fileUpload("/file-upload", "Upload File"),
    imageUpload("/image-upload", "Upload Images"),
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
    br_checker_export_settings("/br-details/export-settings", "Export Settings"),
    searchCluster("/search-cluster", "Search Cluster"),
    BrCheckerImages("/br-checker/images", "Br Checker Images"),
    googleMap("/google-map", "Google Map"),
    searchConsumerMobile("/search-consumer-mobile", "Search Consumer Mobile"),
    searchConsumerName("/search-consumer-name", "Search Consumer Name"),
    searchAuditorCode("/search-auditor-code", "Search Auditor Code"),
    searchTsrCode("/search-tsr-code", "Search TSR Code"), workDayDetails("/call/work-day-details", "Work Day Details");

    public final String value;
    public final String label;

    Uris(final String value, String label) {
        this.value = value;
        this.label = label;
    }
}
