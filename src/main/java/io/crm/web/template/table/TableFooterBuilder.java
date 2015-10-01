package io.crm.web.template.table;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TableFooterBuilder {
    private String id;
    private String classes;
    private List<TableRow> tableRows;

    public TableFooterBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public TableFooterBuilder setClasses(String classes) {
        this.classes = classes;
        return this;
    }

    public TableFooterBuilder setTableRows(List<TableRow> tableRows) {
        this.tableRows = tableRows;
        return this;
    }

    public TableFooterBuilder addTableRows(Consumer<List<TableRow>> consumer) {
        final ArrayList<TableRow> list = new ArrayList<>();
        consumer.accept(list);
        tableRows = list;
        return this;
    }

    public TableFooter createTableFooter() {
        return new TableFooter(id, classes, tableRows);
    }
}