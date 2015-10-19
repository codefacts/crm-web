package io.crm.web.controller;

import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.template.*;
import io.crm.web.template.form.InputBuilder;
import io.crm.web.template.form.RangeInputBuilder;
import io.crm.web.template.page.CallDetailsTemplateBuilder;
import io.crm.web.template.page.DashboardTemplateBuilder;
import io.crm.web.util.Pagination;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.watertemplate.Template;

import static java.util.Collections.EMPTY_LIST;

/**
 * Created by someone on 23/09/2015.
 */
final public class CallController {
    private final Vertx vertx;

    public CallController(final Vertx vertx, final Router router) {
        this.vertx = vertx;
        details(router);
    }

    private void details(final Router router) {
        router.get(Uris.callDetails.value).handler(ctx -> {
            ctx.response().end(
                    new PageBuilder(Uris.callDetails.label)
                            .body(
                                    new DashboardTemplateBuilder()
                                            .setUser(ctx.session().get(ST.currentUser))
                                            .setSidebarTemplate(
                                                    new SidebarTemplateBuilder()
                                                            .setCurrentUri(ctx.request().uri())
                                                            .createSidebarTemplate()
                                            )
                                            .setContentTemplate(
                                                    new CallDetailsTemplateBuilder()
                                                            .setFiltersPanel(
                                                                    filtersPanel()
                                                            )
                                                            .setDataPanel(
                                                                    dataPanel()
                                                            )
                                                            .build()
                                            )
                                            .build()
                            )
                            .build().render());
        });
    }

    private Template dataPanel() {
        return
                new DataPanelTemplateBuilder("Data")
                        .setHeader(null)
                        .setData(EMPTY_LIST)
                        .pagination(null, new Pagination(0, 20, 0), 10)
                        .setFooter(new JsonObject())
                        .build();
    }

    private Template filtersPanel() {
        return
                new FiltersPanelTemplateBuilder("Filters")
                        .configureForm(form -> {
                            form.addRow(builder -> {
                                builder
                                        .addSelectInput(
                                                new InputBuilder<>()
                                                        .setName("test")
                                                        .setColumnClasses("col-md-2"))
                                        .addTextInput(new InputBuilder<>()
                                                .setPlaceholder("Plsc").setColumnClasses("col-md-2"))
                                        .addRangeInput(new RangeInputBuilder<Number>()
                                                .setPlaceholderFrom("From")
                                                .setPlaceholderTo("To").setColumnClasses("col-md-2"))
                                        .addDateInput(new InputBuilder<>()
                                                .setPlaceholder("Plsc").setColumnClasses("col-md-2"))
                                        .addNumberInput(new InputBuilder<>()
                                                .setPlaceholder("Plsc").setColumnClasses("col-md-2"))
                                        .addDateRangeInput(new RangeInputBuilder<>()
                                                .setPlaceholderFrom("from")
                                                .setPlaceholderTo("To").setColumnClasses("col-md-2"))
                                ;
                            }).addRow(builder -> {
                                builder
                                        .addSelectInput(
                                                new InputBuilder<>()
                                                        .setName("test")
                                                        .setColumnClasses("col-md-2"))
                                        .addTextInput(new InputBuilder<>()
                                                .setPlaceholder("Plsc").setColumnClasses("col-md-2"))
                                        .addDateRangeInput(new RangeInputBuilder<>()
                                                .setPlaceholderFrom("from")
                                                .setPlaceholderTo("To").setColumnClasses("col-md-2"))
                                        .addDateInput(new InputBuilder<>()
                                                .setPlaceholder("Plsc").setColumnClasses("col-md-2"))
                                        .addNumberInput(new InputBuilder<>()
                                                .setPlaceholder("Plsc").setColumnClasses("col-md-2"))
                                        .addRangeInput(new RangeInputBuilder<Number>()
                                                .setPlaceholderFrom("From")
                                                .setPlaceholderTo("To").setColumnClasses("col-md-2"))
                                ;
                            }).addRow(builder -> {
                                builder
                                        .addSelectInput(
                                                new InputBuilder<>()
                                                        .setName("test")
                                                        .setColumnClasses("col-md-2"))
                                        .addTextInput(new InputBuilder<>()
                                                .setPlaceholder("Plsc").setColumnClasses("col-md-2"))
                                        .addRangeInput(new RangeInputBuilder<Number>()
                                                .setPlaceholderFrom("From")
                                                .setPlaceholderTo("To").setColumnClasses("col-md-2"))
                                        .addDateRangeInput(new RangeInputBuilder<>()
                                                .setPlaceholderFrom("from")
                                                .setPlaceholderTo("To").setColumnClasses("col-md-2"))
                                        .addDateInput(new InputBuilder<>()
                                                .setPlaceholder("Plsc").setColumnClasses("col-md-2"))
                                        .addNumberInput(new InputBuilder<>()
                                                .setPlaceholder("Plsc").setColumnClasses("col-md-2"))
                                ;
                            }).defaultFooter();
                        })
                        .build();
    }

    public static void main(String... args) {
        final FormTemplate build = new FormTemplateBuilder()
                .addRow(form -> {
                    form.addSelectInput(
                            new InputBuilder<>()
                                    .setClasses("col-md-2")
                                    .setName("test")
                                    .setColumnClasses("col-md-2"))
                    ;
                })
                .build();
        System.out.println(build.render());
    }
}
