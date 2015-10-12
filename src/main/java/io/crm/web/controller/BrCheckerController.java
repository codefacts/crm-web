package io.crm.web.controller;

import io.crm.web.ApiEvents;
import io.crm.web.WebST;
import io.crm.web.WebUris;
import io.crm.web.service.callreview.model.BrCheckerModel;
import io.crm.web.template.*;
import io.crm.web.util.Pagination;
import io.crm.web.util.WebUtils;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.watertemplate.Template;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.crm.web.controller.Controllers.DEFAULT_PAGE_SIZE;
import static io.crm.web.util.WebUtils.parseInt;
import static io.crm.web.util.WebUtils.webHandler;

/**
 * Created by sohan on 10/2/2015.
 */
public class BrCheckerController {
    private static final int PAGINATION_NAV_LENGTH = 20;
    private final Vertx vertx;
    private final String title = "Br Checker Details";

    public BrCheckerController(final Router router, Vertx vertx) {
        this.vertx = vertx;
        view(router);
        details(router);
    }

    public void view(final Router router) {
        router.get(WebUris.br_checker_view.value).handler(WebUtils.webHandler(ctx -> {
            final int id = Integer.parseInt(ctx.request().params().get("id"));
            vertx.eventBus().send(ApiEvents.FIND_ONE_BR_CHECKER_INFO, id, WebUtils.catchHandler((AsyncResult<Message<JsonObject>> r) -> {
                if (r.failed()) {
                    ctx.fail(r.cause());
                    return;
                }

                final JsonObject data = r.result().body().getJsonObject(WebST.data, new JsonObject());

                renderSingleOne(ctx, data);
            }, ctx));
        }));
    }

    private void renderSingleOne(final RoutingContext ctx, final JsonObject data) {
        ctx.response().end(
                new PageBuilder(WebUris.callDetails.label)
                        .body(
                                new DashboardTemplateBuilder()
                                        .setUser(ctx.session().get(WebST.currentUser))
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
        router.get(WebUris.br_checker_details.value).handler(webHandler(ctx -> {
            vertx.eventBus().send(ApiEvents.BR_CHECKER_DETAILS,
                    new JsonObject()
                            .put(WebST.page, parseInt(ctx.request().params().get(WebST.page), 1))
                            .put(WebST.size, parseInt(ctx.request().params().get(WebST.size), DEFAULT_PAGE_SIZE)),
                    (AsyncResult<Message<JsonObject>> r) -> {
                        if (r.failed()) {
                            ctx.fail(r.cause());
                            return;
                        }

                        final JsonObject pagination = r.result().body().getJsonObject(WebST.pagination, new JsonObject());
                        final List<JsonObject> data = r.result().body().getJsonArray(WebST.data, new JsonArray()).getList();
                        System.out.println(data);

                        ctx.response().end(
                                new PageBuilder("Br Checker Data")
                                        .body(
                                                new DashboardTemplateBuilder()
                                                        .setUser(ctx.session().get(WebST.currentUser))
                                                        .setContentTemplate(
                                                                new BrCheckerDetailsTemplateBuilder()
                                                                        .setDataPanel(
                                                                                dataPanel(header(), data, null, pagination, ctx.request().path()))
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
        }));
    }

    private JsonObject header() {
        final JsonObject jsonObject = new JsonObject();
        Arrays.asList(BrCheckerModel.values()).forEach(v -> {
            jsonObject.put("image", "Image");
            if (v.visible) {
                jsonObject.put(v.name(), v.label);
            }
        });
        return jsonObject;
    }

    public DataPanelTemplate dataPanel(final JsonObject header, final List<JsonObject> data, final JsonObject footer, final JsonObject paginationObject, final String uriPath) {
        Pagination pagination = new Pagination(paginationObject.getInteger(WebST.page, 1), paginationObject.getInteger(WebST.size, 20), paginationObject.getLong(WebST.total, 0L));
        return
                new DataPanelTemplateBuilder(String.format(title + " [%d data found]", paginationObject.getLong(WebST.total)))
                        .setHeader(header)
                        .setFooter(footer)
                        .setData(
                                data
                                        .stream()
                                        .map(j -> {
                                            final JsonObject object = new JsonObject();
                                            final String id = String.format("<a href=\"%s\">%s</a>", WebUris.br_checker_view.value + "?id=" + j.getInteger("id"),
                                                    String.format("<img src=\"/br-checker/images?name=%s\" style=\"max-height: 57px;\"/>", j.getString(BrCheckerModel.PICTURE_NAME.name())));
                                            object.put("image", id);
                                            j.getMap().forEach((k, v) -> {
                                                if (k.equals(BrCheckerModel.BAND.name()))
                                                    object.put(k, ((Integer) v) > 0 ? "Yes" : "No");
                                                else if (k.equals(BrCheckerModel.CONTACTED.name()))
                                                    object.put(k, ((Integer) v) > 0 ? "Yes" : "No");
                                                else if (k.equals(BrCheckerModel.NAME_MATHCH.name()))
                                                    object.put(k, ((Integer) v) > 0 ? "Yes" : "No");
                                                else object.put(k, v == null ? "" : v + "");
                                            });
                                            return object;
                                        })
                                        .collect(Collectors.toList())
                        )
                        .setPaginationTemplate(
                                WebUtils.createPaginationTemplate(uriPath, pagination, PAGINATION_NAV_LENGTH)
                        )
                        .build();
    }
}
