package io.crm.web.controller;

import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.template.*;
import io.crm.web.template.form.InputBuilder;
import io.crm.web.template.page.ReactDOMBinder;
import io.crm.web.template.page.DashboardTemplateBuilder;
import io.crm.web.template.page.js.CallDetailsSummaryViewJS;
import io.crm.web.template.page.js.CallDetailsTemplateJS;
import io.crm.web.template.page.js.WorkDayDetailsJS;
import io.crm.web.util.WebUtils;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

/**
 * Created by someone on 23/09/2015.
 */
final public class CallController {
    private final Vertx vertx;

    public CallController(final Vertx vertx, final Router router) {
        this.vertx = vertx;
        details(router);
        workDayDetails(router);
    }

    private void details(final Router router) {
        router.get(Uris.callDetails.value).handler(WebUtils.webHandler(ctx -> {
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
                                                    new ReactDOMBinder(
                                                            new CallDetailsTemplateJS(
                                                                    new CallDetailsSummaryViewJS().render()
                                                            ).render()
                                                    )
                                            )
                                            .build()
                            )
                            .build().render());
        }));
    }

    private void workDayDetails(final Router router) {
        router.get(Uris.workDayDetails.value).handler(WebUtils.webHandler(ctx -> {
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
                                                    new ReactDOMBinder(
                                                            new WorkDayDetailsJS(
                                                                    new CallDetailsSummaryViewJS().render()
                                                            ).render()
                                                    )
                                            )
                                            .build()
                            )
                            .build().render());
        }));
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
