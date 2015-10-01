package io.crm.web.template;

import com.google.common.collect.ImmutableList;
import io.crm.util.Util;
import io.crm.web.css.bootstrap.BootstrapCss;
import io.crm.web.css.bootstrap.TableClasses;
import io.crm.web.template.model.Footer;
import io.crm.web.template.model.Header;
import io.crm.web.template.pagination.PaginationTemplate;
import io.crm.web.template.table.*;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.crm.util.Util.getOrDefault;

public class DataPanelTemplateBuilder {
    public static final List<Header> EMPTY_HEADERS = ImmutableList.of();
    public static final List<Footer> EMPTY_FOOTERS = ImmutableList.of();
    public static final List<JsonObject> EMPTY_DATA = ImmutableList.of();
    private final String title;
    private List<JsonObject> data;
    private List<Header> headers;
    private List<Footer> footers;
    private PaginationTemplate paginationTemplate;
    private final TableTemplateBuilder tableTemplateBuilder = new TableTemplateBuilder();

    public DataPanelTemplateBuilder(String title) {
        this.title = title;
    }

    public DataPanelTemplateBuilder setPaginationTemplate(PaginationTemplate paginationTemplate) {
        this.paginationTemplate = paginationTemplate;
        return this;
    }

    public DataPanelTemplateBuilder setFooters(List<Footer> footers) {
        this.footers = footers;
        return this;
    }

    public DataPanelTemplateBuilder setHeaders(List<Header> headers) {
        this.headers = headers;
        return this;
    }

    public DataPanelTemplateBuilder setData(List<JsonObject> data) {
        this.data = data;
        return this;
    }

    public DataPanelTemplate build() {
        tableTemplateBuilder
                .addClass(TableClasses.STRIPED.value)
                .setHeader(
                        new TableHeaderBuilder()
                                .addTableRows(rows -> {
                                    rows.add(
                                            new TableRowBuilder()
                                                    .addTableCells(cells -> {
                                                        final List<Header> list = getOrDefault(headers, EMPTY_HEADERS);
                                                        list.forEach(h -> {
                                                            cells.add(
                                                                    new ThBuilder()
                                                                            .setBody(h.label)
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
                                                            final List<Header> list = getOrDefault(headers, EMPTY_HEADERS);
                                                            list.forEach(h -> {
                                                                cells.add(
                                                                        new TableCellBuilder()
                                                                                .setBody(json.getString(h.field))
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
                                    rows.add(
                                            new TableRowBuilder()
                                                    .addTableCells(cells -> {
                                                        final List<Footer> list = getOrDefault(footers, EMPTY_FOOTERS);
                                                        list.forEach(f -> {
                                                            cells.add(
                                                                    new TableCellBuilder()
                                                                            .setBody(f.label)
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

        return new DataPanelTemplate(title, tableTemplateBuilder.createTableTemplate(),
                headers, paginationTemplate);
    }

}