package io.crm.web.controller;

import io.crm.util.Util;
import io.crm.web.Uris;
import io.crm.web.template.GoogleMapTemplate;
import io.vertx.ext.web.Router;

/**
 * Created by someone on 04/10/2015.
 */
public class GoogleMapController {
    public GoogleMapController(final Router router) {
        view(router);
    }

    public void view(final Router router) {
        router.get(Uris.googleMap.value).handler(ctx -> {
            ctx.response().end(
                    new GoogleMapTemplate(
                            Util.getOrDefault(ctx.request().params().get("marker_title"), ""),
                            Double.parseDouble(ctx.request().params().get("lat")),
                            Double.parseDouble(ctx.request().params().get("lng"))).render()
            );
        });
    }
}
