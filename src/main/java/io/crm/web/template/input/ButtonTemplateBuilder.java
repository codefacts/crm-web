package io.crm.web.template.input;

public class ButtonTemplateBuilder {
    private String id;
    private String classes;
    private String btnText;

    public ButtonTemplateBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public ButtonTemplateBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public ButtonTemplateBuilder setBtnText(String btnText) {
        this.btnText = btnText;
        return this;
    }

    public ButtonTemplate createButtonTemplate() {
        return new ButtonTemplate(id, classes, btnText);
    }
}