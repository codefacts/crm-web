package io.crm.web.template.bootstrap;

import java.util.HashSet;
import java.util.Set;

public class ModalAlertBuilder {
    private String id;
    private Set<String> classes = new HashSet<>();
    private String title;
    private String body;
    private boolean show;

    public ModalAlertBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public ModalAlertBuilder show() {
        return setShow(true);
    }

    public boolean isShow() {
        return show;
    }

    public ModalAlertBuilder setShow(boolean show) {
        this.show = show;
        return this;
    }

    public ModalAlertBuilder addClass(final String clasz) {
        classes.add(clasz);
        return this;
    }

    public ModalAlertBuilder removeClass(final String clasz) {
        classes.remove(clasz);
        return this;
    }

    public ModalAlertBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public ModalAlertBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public ModalAlert createModalAlert() {
        return new ModalAlert(id, String.join(" ", classes), title, body, show);
    }
}