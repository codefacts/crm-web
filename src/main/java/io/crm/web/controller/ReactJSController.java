package io.crm.web.controller;

import io.crm.util.ExceptionUtil;
import io.crm.web.Uris;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import org.watertemplate.Template;

import java.util.Map;

/**
 * Created by someone on 25/11/2015.
 */
public class ReactJSController {
    public static final String TEMPLATE_NAME = "templateName";
    public final Map<String, Class<? extends Template>> classMap;
    private final Vertx vertx;
    private final Router router;

    public ReactJSController(Map<String, Class<? extends Template>> classMap, Vertx vertx, Router router) {
        this.classMap = classMap;
        this.vertx = vertx;
        this.router = router;
        reactJS(router);
    }

    public void reactJS(Router router) {
        router.get(Uris.REACT_JS_TEMPLATE.value).handler(ctx -> {
            ctx.response().headers().set("Content-Type", "application/javascript");
            ctx.response().end(
                    ExceptionUtil.toRuntimeCall(() ->
                            classMap.get(ctx.request().getParam(TEMPLATE_NAME)).newInstance()).render()
            );
        });
    }
}
