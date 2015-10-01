package io.crm.web.template.table;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TableBodyBuilder {
    private String id;
    private String classes;
    private List<TableRow> tableRows;

    public TableBodyBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public TableBodyBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public TableBodyBuilder setTableRows(List<TableRow> tableRows) {
        this.tableRows = tableRows;
        return this;
    }

    public TableBodyBuilder addTableRows(Consumer<List<TableRow>> consumer) {
        final ArrayList<TableRow> list = new ArrayList<>();
        consumer.accept(list);
        tableRows = list;
        return this;
    }

    public TableBody createTableBody() {
        return new TableBody(id, classes, tableRows);
    }
}