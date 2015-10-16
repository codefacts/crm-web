package io.crm.web.template.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

public class TableHeaderBuilder {
    private String id;
    private String classes;
    private Collection<TableRow> tableRows;

    public TableHeaderBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public TableHeaderBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public TableHeaderBuilder setTableRows(Collection<TableRow> tableRows) {
        this.tableRows = tableRows;
        return this;
    }

    public TableHeaderBuilder addTableRows(Consumer<List<TableRow>> consumer) {
        final ArrayList<TableRow> list = new ArrayList<>();
        consumer.accept(list);
        tableRows = list;
        return this;
    }

    public TableHeader createTableHeader() {
        return new TableHeader(id, classes, tableRows);
    }
}