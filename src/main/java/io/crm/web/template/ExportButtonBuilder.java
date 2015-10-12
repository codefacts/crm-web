package io.crm.web.template;

public class ExportButtonBuilder {
    private String exportLink;
    private String clasz;
    private String label;

    public ExportButtonBuilder setExportLink(String exportLink) {
        this.exportLink = exportLink;
        return this;
    }

    public ExportButtonBuilder setClasz(String clasz) {
        this.clasz = clasz;
        return this;
    }

    public ExportButtonBuilder setLabel(String label) {
        this.label = label;
        return this;
    }

    public ExportButton createExportButton() {
        return new ExportButton(exportLink, clasz, label);
    }
}