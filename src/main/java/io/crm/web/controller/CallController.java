package io.crm.web.controller;

import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.service.ApiService;
import io.crm.web.template.*;
import io.vertx.ext.web.Router;

/**
 * Created by someone on 23/09/2015.
 */
final public class CallController {
    private final ApiService apiService;

    public CallController(final ApiService apiService, final Router router) {
        this.apiService = apiService;
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
                                                    new SidebarTemplate(ctx.request().uri())
                                            )
                                            .setContentTemplate(
                                                    new CallDetailsTemplateBuilder()
                                                            .setFiltersPanel(
                                                                    new FiltersPanelTemplateBuilder("Filters")
                                                                            .build()
                                                            )
                                                            .setDataPanel(
                                                                    new DataPanelTemplateBuilder("Data")
                                                                            .build()
                                                            )
                                                            .build()
                                            )
                                            .build()
                            )
                            .build().render());
        });
    }
}
