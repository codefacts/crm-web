package io.crm.web.template.input;

import io.crm.web.css.bootstrap.BootstrapCss;
import io.crm.web.css.bootstrap.ButtonClasses;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ButtonTemplateBuilder {
    private String id;
    private String btnText;
    private final Set<String> classSet = new HashSet<>();

    public ButtonTemplateBuilder setId(final String id) {
        this.id = id;
        return this;
    }

    public ButtonTemplateBuilder pullRight() {
        addClass(BootstrapCss.PULL_RIGHT.value);
        return this;
    }

    public ButtonTemplateBuilder pullLeft() {
        addClass(BootstrapCss.PULL_LEFT.value);
        return this;
    }

    public ButtonTemplateBuilder primary() {
        addClass(ButtonClasses.BTN_PRIMARY.value);
        return this;
    }

    public ButtonTemplateBuilder success() {
        addClass(ButtonClasses.BTN_SUCCESS.value);
        return this;
    }

    public ButtonTemplateBuilder danger() {
        addClass(ButtonClasses.BTN_DANGER.value);
        return this;
    }

    public ButtonTemplateBuilder warning() {
        addClass(ButtonClasses.BTN_WARNING.value);
        return this;
    }

    public ButtonTemplateBuilder info() {
        addClass(ButtonClasses.BTN_INFO.value);
        return this;
    }

    public ButtonTemplateBuilder defaultClass() {
        addClass(ButtonClasses.BTN_INFO.value);
        return this;
    }

    public ButtonTemplateBuilder link() {
        addClass(ButtonClasses.BTN_LINK.value);
        return this;
    }

    public ButtonTemplateBuilder addClasses(final Collection<String> classes) {
        classSet.addAll(classes);
        return this;
    }

    public ButtonTemplateBuilder addClass(final String clasz) {
        classSet.add(clasz);
        return this;
    }

    public ButtonTemplateBuilder removeClasses(final Set<String> classes) {
        classSet.removeAll(classes);
        return this;
    }

    public ButtonTemplateBuilder removeClass(final String clasz) {
        classSet.remove(clasz);
        return this;
    }

    public ButtonTemplateBuilder setBtnText(final String btnText) {
        this.btnText = btnText;
        return this;
    }

    public ButtonTemplate createButtonTemplate() {
        return new ButtonTemplate(id, String.join(" ", classSet), btnText);
    }
}