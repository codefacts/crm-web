package io.crm.web.controller;

import io.crm.util.Util;
import io.crm.web.Uris;
import io.crm.web.template.page.GoogleMapTemplate;
import io.crm.web.util.Converters;
import io.vertx.ext.web.Router;

/**
 * Created by someone on 04/10/2015.
 */
public class GoogleMapController {
    public GoogleMapController(final Router router) {
        view(router);
    }

    public void view(final Router router) {
        router.get(Uris.GOOGLE_MAP.value).handler(ctx -> {
            ctx.response().end(
                    new GoogleMapTemplate(
                            Util.or(ctx.request().params().get("marker_title"), ""),
                            Converters.toDouble(ctx.request().params().get("lat")),
                            Converters.toDouble(ctx.request().params().get("lng"))).render()
            );
        });
    }
}
