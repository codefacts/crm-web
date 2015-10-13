package io.crm.web.template.bootstrap;

import java.util.HashSet;
import java.util.Set;

public class BodyPanelDefaultBuilder {
    private String body;
    private Set<String> body_classes = new HashSet<>();

    public BodyPanelDefaultBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public BodyPanelDefaultBuilder addBodyClass(final String clasz) {
        body_classes.add(clasz);
        return this;
    }

    public BodyPanelDefaultBuilder removeBodyClass(final String clasz) {
        body_classes.remove(clasz);
        return this;
    }

    public BodyPanelDefault createBodyPanelDefault() {
        return new BodyPanelDefault(body, String.join(" ", body_classes));
    }
}