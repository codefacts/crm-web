package io.crm.web.template.page.renderer;

import com.google.common.collect.ImmutableMap;
import io.crm.util.ExceptionUtil;
import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.service.callreview.BrCheckerDetailsService;
import io.crm.web.service.callreview.model.BrCheckerModel;
import io.crm.web.template.*;
import io.crm.web.template.form.DefaultFooterBuilder;
import io.crm.web.template.page.BrCheckerDetailsFilter;
import io.crm.web.template.page.BrCheckerDetailsTemplateBuilder;
import io.crm.web.template.page.BrCheckerExportSettings;
import io.crm.web.template.page.DashboardTemplateBuilder;
import io.crm.web.util.Pagination;
import io.crm.web.util.WebUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import org.watertemplate.Template;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by someone on 19/10/2015.
 */
final public class BrDetailsRenderer {
    private static final int PAGINATION_NAV_LENGTH = 15;
    final RoutingContext ctx;
    private final String title;
    final JsonObject pagination;
    final List<JsonObject> data;
    private final String queryString;
    private final List<String> callStatuses;

    BrDetailsRenderer(final RoutingContext ctx, final List<String> callStatuses, final String title,
                      final JsonObject pagination, final List<JsonObject> data) {

        this.ctx = ctx;
        this.title = title;
        this.pagination = pagination;
        this.data = data;
        this.callStatuses = callStatuses;

        final StringBuilder builder = new StringBuilder();
        ctx.request().params().remove(ST.page).remove(ST.size);
        ctx.request().params().forEach(e -> builder
                .append(e.getKey())
                .append("=")
                .append(ExceptionUtil.sallowCall(() -> URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8.name())))
                .append("&"));
        queryString = builder.length() > 0 ? builder.deleteCharAt(builder.length() - 1).toString() : "";
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
                                            final String id = String.format("<a href=\"%s\">%s</a>", imageUrl(j),
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
                                WebUtils.createPaginationTemplateBuilder(uriPath, queryString, pagination, PAGINATION_NAV_LENGTH).createPaginationTemplate()
                        )
                        .build();
    }

    private String imageUrl(JsonObject j) {
        return Uris.br_checker_view.value + "?id=" + j.getInteger("id");
    }

    public String render() {
        return new PageBuilder("Br Checker Data")
                .body(
                        new DashboardTemplateBuilder()
                                .setUser(ctx.session().get(ST.currentUser))
                                .setContentTemplate(
                                        new BrCheckerDetailsTemplateBuilder()
                                                .setFiltersPanel(
                                                        TemplateUtil.EMPTY_TEMPLATE
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
                .build().render();
    }

    private Template filtersPanel() {
        return new FiltersPanelTemplateBuilder("Filters")
                .configureForm(form -> {
                    form
                            .addRow(row -> {
                                row
                                        .addSelectInput("cluster", "ajax-select", "cluster", ImmutableMap.of("", "Cluster", "1", "1", "2", "1", "3", "1"), "col-md-3")
                                        .addSelectInput("tsr_code", "ajax-select", "tsr_code", ImmutableMap.of("", "TSR Code"), "col-md-2")
                                        .addSelectInput("auditor_code", "ajax-select", "auditor_code", ImmutableMap.of("", "Auditor Code"), "col-md-2")
                                        .addSelectInput("consumer_name", "ajax-select", "consumer_name", ImmutableMap.of("", "Consumer Name"), "col-md-3")
                                        .addSelectInput("consumer_mobile", "ajax-select", "consumer_mobile", ImmutableMap.of("", "Consumer Mobile"), "col-md-2")
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
                                                                                        .search()
                                                                                        .export()
                                                                                        .clear()
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
                .build();
    }
}
