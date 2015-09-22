package io.crm.web.controller;

import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.view.DashboardTemplate;
import io.crm.web.view.PageBuilder;
import io.vertx.ext.web.Router;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;

/**
 * Created by someone on 22/09/2015.
 */
public class HomeController {
    private final Router router;

    public HomeController(Router router) {
        this.router = router;
    }

    public void index() {
        router.get(Uris.dashboard.value).handler(context -> {
            context.response().headers().set(CONTENT_TYPE, "text/html");
            context.response().end(
                    new PageBuilder("Dashboard")
                            .body(new DashboardTemplate(context.session().get(ST.currentUser)))
                            .build().render());
        });
    }
}
