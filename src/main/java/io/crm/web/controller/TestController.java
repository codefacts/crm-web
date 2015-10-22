package io.crm.web.controller;

import io.crm.web.ST;
import io.crm.web.template.PageBuilder;
import io.crm.web.template.bootstrap.ModalAlertBuilder;
import io.crm.web.template.page.DashboardTemplateBuilder;
import io.crm.web.template.react.AjaxSelect;
import io.vertx.ext.web.Router;

import static io.crm.web.util.WebUtils.webHandler;

/**
 * Created by someone on 20/10/2015.
 */
public class TestController {

    public void testController(final Router router) {
        router.get("/test").handler(webHandler(ctx -> {
            ctx.response().end(
                    new PageBuilder("Test")
                            .body(
                                    new DashboardTemplateBuilder()
                                            .setUser(ctx.session().get(ST.currentUser))
                                            .setContentTemplate(
                                                    new AjaxSelect()
                                            )
                                            .build()
                            )
                            .build().render()
            );
        }));
    }


}
