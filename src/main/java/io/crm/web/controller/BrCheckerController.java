package io.crm.web.controller;

import io.crm.promise.intfs.Promise;
import io.crm.util.Util;
import io.crm.web.ApiEvents;
import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.service.callreview.BrCheckerDetailsService;
import io.crm.web.template.*;
import io.crm.web.template.page.BrCheckerExportSettings;
import io.crm.web.template.page.BrCheckerInfoView;
import io.crm.web.template.page.DashboardTemplateBuilder;
import io.crm.web.template.page.renderer.BrDetailsRendererBuilder;
import io.crm.web.util.WebUtils;
import io.vertx.core.AsyncResult;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.watertemplate.Template;

import java.util.Collections;
import java.util.List;

import static io.crm.web.Uris.br_checker_export_settings;
import static io.crm.web.controller.Controllers.DEFAULT_PAGE_SIZE;
import static io.crm.web.util.WebUtils.parseInt;
import static io.crm.web.util.WebUtils.webHandler;

/**
 * Created by sohan on 10/2/2015.
 */
public class BrCheckerController {
    private static final int PAGINATION_NAV_LENGTH = 15;
    private final Vertx vertx;
    private final String title = "Br Checker Details";

    public BrCheckerController(final Router router, Vertx vertx) {
        this.vertx = vertx;
        view(router);
        details(router);
        exportSettings(router);
    }

    private void exportSettings(final Router router) {
        router.get(br_checker_export_settings.value).handler(WebUtils.webHandler(
                ctx -> {
                    ctx.response().end(
                            new PageBuilder(Uris.br_checker_export_settings.label)
                                    .body(
                                            new BrCheckerExportSettings(BrCheckerDetailsService.baseUrl() + "/brCHecker/export")
                                    )
                                    .build().render()
                    );
                }));
    }

    public void view(final Router router) {
        router.get(Uris.br_checker_view.value).handler(WebUtils.webHandler(ctx -> {
            final int id = Integer.parseInt(ctx.request().params().get("id"));
            vertx.eventBus().send(ApiEvents.FIND_ONE_BR_CHECKER_INFO, id, WebUtils.catchHandler((AsyncResult<Message<JsonObject>> r) -> {
                if (r.failed()) {
                    ctx.fail(r.cause());
                    return;
                }

                final JsonObject data = r.result().body().getJsonObject(ST.data, new JsonObject());

                renderSingleOne(ctx, data);
            }, ctx));
        }));
    }

    private void renderSingleOne(final RoutingContext ctx, final JsonObject data) {
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
                                                renderSingle(ctx, data)
                                        )
                                        .build()
                        )
                        .build().render());
    }

    private Template renderSingle(final RoutingContext ctx, final JsonObject data) {
        final BrCheckerInfoView brCheckerInfoView = new BrCheckerInfoView(data);
        return brCheckerInfoView;
    }

    public void details(final Router router) {
        router.get(Uris.br_checker_details.value).handler(webHandler(ctx -> {

            final MultiMap params = ctx.request().params();

            if (ST.clear.equals(params.get(ST.Form.__action__))) {
                WebUtils.redirect(Uris.br_checker_details.value, ctx.response());
                return;
            }

            params.remove(ST.Form.__action__);

            final JsonObject paramsJson = new JsonObject();
            params.names().forEach(name -> {
                paramsJson
                        .put(name, params.getAll(name).size() > 1 ? params.getAll(name) : params.get(name));
            });

            final Promise<Message<JsonObject>> promise2 = Util.<JsonObject>send(vertx.eventBus(), ApiEvents.BR_CHECKER_DETAILS,
                    new JsonObject()
                            .put(ST.page, parseInt(params.get(ST.page), 1))
                            .put(ST.size, parseInt(params.get(ST.size), DEFAULT_PAGE_SIZE))
                            .put(ST.params, paramsJson))

                    .then(v -> {

                        final JsonObject pagination = v.body().getJsonObject(ST.pagination, new JsonObject());
                        final List<JsonObject> data = v.body().getJsonArray(ST.data, new JsonArray()).getList();

                        ctx.response().end(
                                new BrDetailsRendererBuilder()
                                        .setCallStatuses(Collections.<String>emptyList())
                                        .setCtx(ctx)
                                        .setTitle(title)
                                        .setPagination(pagination)
                                        .setData(data)
                                        .createBrDetailsRenderer()
                                        .render()
                        );
                    })
                    .error(ctx::fail);
        }));
    }

}
