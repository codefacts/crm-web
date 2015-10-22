package io.crm.web.controller;

import io.crm.util.Util;
import io.crm.web.ApiEvents;
import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.util.Converters;
import io.crm.web.util.WebUtils;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.util.List;

/**
 * Created by someone on 20/10/2015.
 */
final public class BrCheckerJsonController {
    final Vertx vertx;
    final Router router;

    public BrCheckerJsonController(final Vertx vertx, final Router router) {
        this.vertx = vertx;
        this.router = router;
        searchClusterAjax(router);
    }

    public void searchClusterAjax(final Router router) {
        router.get(Uris.searchCluster.value).handler(WebUtils.webHandler(ctx -> {

            final JsonObject criteria = new JsonObject();
            final MultiMap params = ctx.request().params();

            criteria.put(ST.page, Converters.toInt(params.get(ST.page)));
            criteria.put(ST.size, Converters.toInt(params.get(ST.size)));
            if (params.contains(ST.name)) criteria.put(ST.name, params.get(ST.name));

            Util.<JsonObject>send(vertx.eventBus(), ApiEvents.SEARCH_CLUSTER, criteria)
                    .error(ctx::fail)
                    .success(message -> {
                        ctx.response().end(
                                message.body().encodePrettily()
                        );
                    })
            ;
        }));
    }
}
