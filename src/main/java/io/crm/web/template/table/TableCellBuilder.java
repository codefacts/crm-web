package io.crm.web.template.table;

import org.watertemplate.Template;

public class TableCellBuilder {
    private String id;
    private String classes;
    private String body;

    public TableCellBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public TableCellBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public TableCellBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public TableCell createTableCell() {
        return new TableCell(id, classes, body);
    }
}