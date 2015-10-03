package io.crm.web.template;

import com.google.common.collect.ImmutableList;
import io.crm.web.css.bootstrap.TableClasses;
import io.crm.web.template.pagination.PaginationTemplate;
import io.crm.web.template.table.*;
import io.vertx.core.json.JsonObject;

import java.util.List;

import static io.crm.util.Util.getOrDefault;

public class DataPanelTemplateBuilder {
    public static final JsonObject EMPTY_HEADER = new JsonObject();
    public static final JsonObject EMPTY_FOOTER = new JsonObject();
    public static final List<JsonObject> EMPTY_DATA = ImmutableList.of();
    private final String title;
    private List<JsonObject> data;
    private JsonObject header;
    private JsonObject footer;
    private PaginationTemplate paginationTemplate;
    private final TableTemplateBuilder tableTemplateBuilder = new TableTemplateBuilder();

    public DataPanelTemplateBuilder(String title) {
        this.title = title;
    }

    public DataPanelTemplateBuilder setPaginationTemplate(PaginationTemplate paginationTemplate) {
        this.paginationTemplate = paginationTemplate;
        return this;
    }

    public DataPanelTemplateBuilder setFooter(final JsonObject footer) {
        this.footer = footer;
        return this;
    }

    public DataPanelTemplateBuilder setHeader(final JsonObject header) {
        this.header = header;
        return this;
    }

    public DataPanelTemplateBuilder setData(List<JsonObject> data) {
        this.data = data;
        return this;
    }

    public DataPanelTemplate build() {
        tableTemplateBuilder
                .addClass(TableClasses.BORDERED.value)
                .setHeader(
                        new TableHeaderBuilder()
                                .addTableRows(rows -> {
                                    rows.add(
                                            new TableRowBuilder()
                                                    .addTableCells(cells -> {
                                                        final JsonObject headerObject = getOrDefault(header, EMPTY_HEADER);
                                                        headerObject.forEach(e -> {
                                                            cells.add(
                                                                    new ThBuilder()
                                                                            .setBody(e.getValue() != null ? (String) e.getValue() : "")
                                                                            .createTh()
                                                            );
                                                        });
                                                    })
                                                    .createTableRow()
                                    );
                                })
                                .createTableHeader()
                )
                .setBody(
                        new TableBodyBuilder()
                                .addTableRows(rows -> {
                                    final List<JsonObject> listData = getOrDefault(data, EMPTY_DATA);
                                    listData.forEach(json -> {
                                        rows.add(
                                                new TableRowBuilder()
                                                        .addTableCells(cells -> {
                                                            final JsonObject headerObject = getOrDefault(header, EMPTY_HEADER);
                                                            headerObject.forEach(e -> {
                                                                cells.add(
                                                                        new TableCellBuilder()
                                                                                .setBody(json.getString(e.getKey(), ""))
                                                                                .createTableCell()
                                                                );
                                                            });
                                                        })
                                                        .createTableRow()
                                        );
                                    });
                                })
                                .createTableBody()
                )
                .setFooter(
                        new TableFooterBuilder()
                                .addTableRows(rows -> {
                                    final JsonObject footerObject = getOrDefault(footer, EMPTY_FOOTER);
                                    if (footerObject.size() <= 0) return;
                                    rows.add(
                                            new TableRowBuilder()
                                                    .addTableCells(cells -> {
                                                        final JsonObject headerObject = getOrDefault(header, EMPTY_HEADER);
                                                        headerObject.forEach(f -> {
                                                            cells.add(
                                                                    new TableCellBuilder()
                                                                            .setBody(footerObject.getString(f.getKey(), ""))
                                                                            .createTableCell()
                                                            );
                                                        });
                                                    })
                                                    .createTableRow()
                                    );
                                })
                                .createTableFooter()
                )
        ;

        return new DataPanelTemplate(title, tableTemplateBuilder.createTableTemplate(), paginationTemplate);
    }

}