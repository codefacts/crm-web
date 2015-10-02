package io.crm.web.controller;

import io.crm.web.ApiEvents;
import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.css.bootstrap.BootstrapCss;
import io.crm.web.template.*;
import io.crm.web.template.pagination.PaginationItemTemplate;
import io.crm.web.template.pagination.PaginationItemTemplateBuilder;
import io.crm.web.template.pagination.PaginationTemplate;
import io.crm.web.template.pagination.PaginationTemplateBuilder;
import io.crm.web.util.Pagination;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.util.List;

/**
 * Created by sohan on 10/2/2015.
 */
public class BrCheckerController {
    private final Vertx vertx;
    private final String title = "Br Checker Details";

    public BrCheckerController(final Router router, Vertx vertx) {
        this.vertx = vertx;
        details(router);
    }

    public void details(final Router router) {
        router.get(Uris.br_checker_details.value).handler(ctx -> {
            vertx.eventBus().send(ApiEvents.BR_CHECKER_DETAILS,
                    new JsonObject(), (AsyncResult<Message<JsonObject>> r) -> {
                        if (r.failed()) {
                            ctx.fail(r.cause());
                            return;
                        }

                        final JsonObject pagination = r.result().body().getJsonObject(ST.pagination, new JsonObject());
                        final List<JsonObject> data = r.result().body().getJsonArray(ST.data, new JsonArray()).getList();
                        System.out.println(data);

                        ctx.response().end(
                                new PageBuilder("Br Checker Data")
                                        .body(
                                                new DashboardTemplateBuilder()
                                                        .setUser(ctx.session().get(ST.currentUser))
                                                        .setContentTemplate(
                                                                new BrCheckerDetailsTemplateBuilder()
                                                                        .setDataPanel(dataPanel(header(), data, null, pagination))
                                                                        .createBrCheckerDetailsTemplate()
                                                        )
                                                        .setSidebarTemplate(
                                                                new SidebarTemplateBuilder()
                                                                        .setCurrentUri(ctx.request().uri())
                                                                        .createSidebarTemplate()
                                                        )
                                                        .build()
                                        )
                                        .build().render()
                        );
                    });
        });
    }

    private JsonObject header() {
        return
                new JsonObject()
                        .put("name", "Name")
                        .put("designation", "Designation");
    }

    public DataPanelTemplate dataPanel(final JsonObject header, final List<JsonObject> data, final JsonObject footer, final JsonObject paginationObject) {
        Pagination pagination = new Pagination(paginationObject.getInteger(ST.page, 1), paginationObject.getInteger(ST.size, 20), paginationObject.getLong(ST.total, 0L));
        return
                new DataPanelTemplateBuilder(title)
                        .setHeader(header)
                        .setFooter(footer)
                        .setData(data)
                        .setPaginationTemplate(
                                new PaginationTemplateBuilder()
                                        .addClass(BootstrapCss.PULL_RIGHT.value)
                                        .prev("")
                                        .addAllItems(items -> {
                                            pagination.nav(5).forEach(p -> items.add(
                                                    new PaginationItemTemplateBuilder()
                                                            .setLabel("" + p)
                                                            .createPaginationItemTemplate()
                                            ));
                                        })
                                        .next("")
                                        .createPaginationTemplate()
                        )
                        .build();
    }
}
