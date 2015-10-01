package io.crm.web.template.table;

import org.watertemplate.Template;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TableRowBuilder {
    private String id;
    private String classes;
    private List<Template> tableCells;

    public TableRowBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public TableRowBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public TableRowBuilder setTableCells(List<Template> tableCells) {
        this.tableCells = tableCells;
        return this;
    }

    public TableRowBuilder addTableCells(Consumer<List<Template>> consumer) {
        final ArrayList<Template> list = new ArrayList<>();
        consumer.accept(list);
        tableCells = list;
        return this;
    }

    public TableRow createTableRow() {
        return new TableRow(id, classes, tableCells);
    }
}