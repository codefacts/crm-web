package io.crm.web.controller;

import io.crm.web.Uris;
import io.vertx.ext.web.Router;

/**
 * Created by sohan on 10/2/2015.
 */
public class BrCheckerController {
    public BrCheckerController(final Router router) {
        details(router);
    }

    public void details(final Router router) {
        router.get(Uris.br_checker_details.value).handler(ctx -> {

        });
    }
}
