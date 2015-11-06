package io.crm.web.controller;

import io.crm.util.Util;
import io.crm.web.ApiEvents;
import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.util.Converters;
import io.crm.web.util.WebUtils;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

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
        searchTsrCodeAjax(router);
        searchAuditorCodeAjax(router);
        searchConsumerNameAjax(router);
        searchConsumerMobileAjax(router);
    }

    public void searchClusterAjax(final Router router) {
        router.get(Uris.searchCluster.value).handler(WebUtils.webHandler(ctx -> {

            final JsonObject criteria = new JsonObject();
            final MultiMap params = ctx.request().params();

            criteria.put(ST.page, Converters.toInt(params.get(ST.page)));
            criteria.put(ST.size, Converters.toInt(params.get(ST.size)));
            if (params.contains(ST.name)) criteria.put(ST.name, params.get(ST.name));

            Util.<JsonObject>send(vertx.eventBus(), ApiEvents.SEARCH_CLUSTER, criteria)
                    .then(message -> {
                        ctx.response().end(
                                message.body().encodePrettily()
                        );
                    })
                    .error(ctx::fail)
            ;
        }));
    }

    public void searchTsrCodeAjax(final Router router) {
        router.get(Uris.searchTsrCode.value).handler(WebUtils.webHandler(ctx -> {

            final JsonObject criteria = new JsonObject();
            final MultiMap params = ctx.request().params();

            criteria.put(ST.page, Converters.toInt(params.get(ST.page)));
            criteria.put(ST.size, Converters.toInt(params.get(ST.size)));
            if (params.contains(ST.name)) criteria.put(ST.name, params.get(ST.name));

            Util.<JsonObject>send(vertx.eventBus(), ApiEvents.SEARCH_TSR_CODE, criteria)
                    .then(message -> {
                        ctx.response().end(
                                message.body().encodePrettily()
                        );
                    })
                    .error(ctx::fail)
            ;
        }));
    }

    public void searchAuditorCodeAjax(final Router router) {
        router.get(Uris.searchAuditorCode.value).handler(WebUtils.webHandler(ctx -> {

            final JsonObject criteria = new JsonObject();
            final MultiMap params = ctx.request().params();

            criteria.put(ST.page, Converters.toInt(params.get(ST.page)));
            criteria.put(ST.size, Converters.toInt(params.get(ST.size)));
            if (params.contains(ST.name)) criteria.put(ST.name, params.get(ST.name));

            Util.<JsonObject>send(vertx.eventBus(), ApiEvents.SEARCH_AUDITOR_CODE, criteria)
                    .then(message -> {
                        ctx.response().end(
                                message.body().encodePrettily()
                        );
                    })
                    .error(ctx::fail)
            ;
        }));
    }

    public void searchConsumerNameAjax(final Router router) {
        router.get(Uris.searchConsumerName.value).handler(WebUtils.webHandler(ctx -> {

            final JsonObject criteria = new JsonObject();
            final MultiMap params = ctx.request().params();

            criteria.put(ST.page, Converters.toInt(params.get(ST.page)));
            criteria.put(ST.size, Converters.toInt(params.get(ST.size)));
            if (params.contains(ST.name)) criteria.put(ST.name, params.get(ST.name));

            Util.<JsonObject>send(vertx.eventBus(), ApiEvents.SEARCH_CONSUMER_NAME, criteria)
                    .then(message -> {
                        ctx.response().end(
                                message.body().encodePrettily()
                        );
                    })
                    .error(ctx::fail)
            ;
        }));
    }

    public void searchConsumerMobileAjax(final Router router) {
        router.get(Uris.searchConsumerMobile.value).handler(WebUtils.webHandler(ctx -> {

            final JsonObject criteria = new JsonObject();
            final MultiMap params = ctx.request().params();

            criteria.put(ST.page, Converters.toInt(params.get(ST.page)));
            criteria.put(ST.size, Converters.toInt(params.get(ST.size)));
            if (params.contains(ST.name)) criteria.put(ST.name, params.get(ST.name));

            Util.<JsonObject>send(vertx.eventBus(), ApiEvents.SEARCH_CONSUMER_MOBILE, criteria)
                    .then(message -> {
                        ctx.response().end(
                                message.body().encodePrettily()
                        );
                    })
                    .error(ctx::fail)
            ;
        }));
    }
}
