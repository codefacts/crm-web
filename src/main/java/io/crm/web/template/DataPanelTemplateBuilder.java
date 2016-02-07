package io.crm.web.template;

import com.google.common.collect.ImmutableList;
import io.crm.util.Util;
import io.crm.web.css.bootstrap.TableClasses;
import io.crm.web.template.pagination.PaginationTemplate;
import io.crm.web.template.table.*;
import io.crm.web.util.Pagination;
import io.crm.web.util.WebUtils;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class DataPanelTemplateBuilder {
    public static final JsonObject EMPTY_HEADER = new JsonObject();
    public static final JsonObject EMPTY_FOOTER = new JsonObject();
    public static final List<JsonObject> EMPTY_DATA = ImmutableList.of();
    private final String title;
    private List<JsonObject> data;
    private JsonObject header;
    private JsonObject footer;
    private PaginationTemplate paginationTemplate;
    private String exportButton;
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

    public DataPanelTemplateBuilder setExportButton(String exportButton) {
        this.exportButton = exportButton;
        return this;
    }

    public DataPanelTemplateBuilder exportButton(final String exportLink, final String label) {
        setExportButton(
                new ExportButtonBuilder()
                        .setExportLink(exportLink)
                        .setLabel(label)
                        .createExportButton().render()
        );
        return this;
    }

    public DataPanelTemplateBuilder pagination(final String uriPath, String queryString, final Pagination pagination, final int PAGINATION_NAV_LENGTH) {
        setPaginationTemplate(
                WebUtils.createPaginationTemplateBuilder(uriPath, queryString, pagination, PAGINATION_NAV_LENGTH)
                        .createPaginationTemplate()
        );
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
                                                        final JsonObject headerObject = Util.or(header, EMPTY_HEADER);
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
                                    final List<JsonObject> listData = Util.or(data, EMPTY_DATA);
                                    listData.forEach(json -> {
                                        rows.add(
                                                new TableRowBuilder()
                                                        .addTableCells(cells -> {
                                                            final JsonObject headerObject = Util.or(header, EMPTY_HEADER);
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
                                    final JsonObject footerObject = Util.or(footer, EMPTY_FOOTER);
                                    if (footerObject.size() <= 0) return;
                                    rows.add(
                                            new TableRowBuilder()
                                                    .addTableCells(cells -> {
                                                        final JsonObject headerObject = Util.or(header, EMPTY_HEADER);
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

        return new DataPanelTemplate(title, tableTemplateBuilder.createTableTemplate(), paginationTemplate, exportButton);
    }

}