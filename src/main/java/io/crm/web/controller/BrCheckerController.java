package io.crm.web.controller;

import com.google.common.collect.ImmutableMap;
import io.crm.web.ApiEvents;
import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.service.callreview.BrCheckerDetailsService;
import io.crm.web.service.callreview.model.BrCheckerModel;
import io.crm.web.template.*;
import io.crm.web.template.form.DefaultFooterBuilder;
import io.crm.web.template.page.BrCheckerDetailsTemplateBuilder;
import io.crm.web.template.page.BrCheckerExportSettings;
import io.crm.web.template.page.BrCheckerInfoView;
import io.crm.web.template.page.DashboardTemplateBuilder;
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
            vertx.eventBus().send(ApiEvents.BR_CHECKER_DETAILS,
                    new JsonObject()
                            .put(ST.page, parseInt(ctx.request().params().get(ST.page), 1))
                            .put(ST.size, parseInt(ctx.request().params().get(ST.size), DEFAULT_PAGE_SIZE)),
                    (AsyncResult<Message<JsonObject>> r) -> {
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
                                                                        .setFiltersPanel(
                                                                                new FiltersPanelTemplateBuilder("Filters")
                                                                                        .configureForm(form -> {
                                                                                            form
                                                                                                    .addRow(row -> {
                                                                                                        row
                                                                                                                .addSelectInput("cluster", "", "cluster", ImmutableMap.of("", "Cluster"), "col-md-3")
                                                                                                                .addSelectInput("tsr_code", "", "tsr_code", ImmutableMap.of("", "TST Code"), "col-md-2")
                                                                                                                .addSelectInput("auditor_code", "", "auditor_code", ImmutableMap.of("", "Auditor Code"), "col-md-2")
                                                                                                                .addSelectInput("consumer_name", "", "consumer_name", ImmutableMap.of("", "Consumer Name"), "col-md-3")
                                                                                                                .addSelectInput("consumer_mobile", "", "consumer_mobile", ImmutableMap.of("", "Consumer Mobile"), "col-md-2")
                                                                                                        ;
                                                                                                    })
                                                                                                    .addRow(row -> {
                                                                                                        row
                                                                                                                .addTextInput("call_range", "range", "call_range", "", "col-md-2", "Call No. Between")
                                                                                                                .addTextInput("visit_range", "range", "visit_range", "", "col-md-2", "Visit Between")
                                                                                                                .addSelectInput("brand", "", "brand", ImmutableMap.of("", "Brand Match"), "col-md-2")
                                                                                                                .addSelectInput("contacted", "", "contacted", ImmutableMap.of("", "Contacted"), "col-md-2")
                                                                                                                .addSelectInput("name_match", "", "name_match", ImmutableMap.of("", "Name Match"), "col-md-2")
                                                                                                                .addSelectInput("call_status", "", "call_status", ImmutableMap.of("", "Call Status"), "col-md-2")
                                                                                                        ;
                                                                                                    })
                                                                                                    .addRow(row -> {
                                                                                                        row
                                                                                                                .addTextInput("date_range", "date_range", "date_range", "", "col-md-4", "Date Between")
                                                                                                                .addTemplate(
                                                                                                                        new ColumnTemplateBuilder()
                                                                                                                                .setClasses("col-md-8")
                                                                                                                                .setContent(
                                                                                                                                        new TemplateListBuilder()
                                                                                                                                                .setTemplates(
                                                                                                                                                        new DefaultFooterBuilder()
                                                                                                                                                                .clear()
                                                                                                                                                                .export()
                                                                                                                                                                .search()
                                                                                                                                                                .build()
                                                                                                                                                )
                                                                                                                                                .createTemplateList()
                                                                                                                                )
                                                                                                                                .createColumnTemplate()
                                                                                                                )
                                                                                                        ;
                                                                                                    })
                                                                                            ;

                                                                                        })
                                                                                        .build()
                                                                        )
                                                                        .setDataPanel(
                                                                                dataPanel(header(), data, null, pagination, ctx.request().path()))
                                                                        .setPopup(
                                                                                new BrCheckerExportSettings(BrCheckerDetailsService.baseUrl() + "/brCHecker/export").render()
                                                                        )
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
        Pagination pagination = new Pagination(paginationObject.getInteger(ST.page, 1), paginationObject.getInteger(ST.size, 20), paginationObject.getLong(ST.total, 0L));
        return
                new DataPanelTemplateBuilder(WebUtils.titleWithTotal(title, paginationObject.getLong(ST.total)))
                        .setHeader(header)
                        .setFooter(footer)
                        .setData(
                                data
                                        .stream()
                                        .map(j -> {
                                            final JsonObject object = new JsonObject();
                                            final String id = String.format("<a href=\"%s\">%s</a>", Uris.br_checker_view.value + "?id=" + j.getInteger("id"),
                                                    String.format("<img src=\"/br-checker/images?name=%s\" style=\"max-height: 57px;\"/>", j.getString(BrCheckerModel.PICTURE_NAME.name())));
                                            object.put("image", id);
                                            j.getMap().forEach((k, v) -> {
                                                if (k.equals(BrCheckerModel.BAND.name()))
                                                    object.put(k, ((Integer) v) > 0 ? "Yes" : "No");
                                                else if (k.equals(BrCheckerModel.CONTACTED.name()))
                                                    object.put(k, ((Integer) v) > 0 ? "Yes" : "No");
                                                else if (k.equals(BrCheckerModel.NAME_MATCH.name()))
                                                    object.put(k, ((Integer) v) > 0 ? "Yes" : "No");
                                                else object.put(k, v == null ? "" : v + "");
                                            });
                                            return object;
                                        })
                                        .collect(Collectors.toList())
                        )
                        .setExportButton("<!-- Button trigger modal -->\n" +
                                "<button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#myModal\">\n" +
                                "Export\n" +
                                "</button>")
                        .setPaginationTemplate(
                                WebUtils.createPaginationTemplateBuilder(uriPath, pagination, PAGINATION_NAV_LENGTH).createPaginationTemplate()
                        )
                        .build();
    }
}
