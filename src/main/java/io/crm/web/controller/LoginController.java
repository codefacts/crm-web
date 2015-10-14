package io.crm.web.controller;

import io.crm.web.ApiEvents;
import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.excpt.ApiServiceException;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

/**
 * Created by someone on 22/09/2015.
 */
final public class LoginController {
    private final Vertx vertx;
    private final Router router;

    public LoginController(final Vertx vertx, Router router) {
        this.vertx = vertx;
        this.router = router;
    }

    public void login() {
        router.post(Uris.login.value).handler(context -> {
            context.request().setExpectMultipart(true);
            context.request().endHandler(b -> {
                String username = context.request().formAttributes().get("username");
                String password = context.request().formAttributes().get("password");
                vertx.eventBus().send(ApiEvents.LOGIN_API,
                        new JsonObject()
                                .put(ST.username, username)
                                .put(ST.password, password),
                        (AsyncResult<Message<JsonObject>> r) -> {
                            if (r.failed()) {
                                final Throwable cause = r.cause();
                                if (cause instanceof ApiServiceException) {
                                    ApiServiceException cs = (ApiServiceException) cause;
                                    context.response().setStatusCode(cs.getCode());
                                    context.response().end(cs.getMessage());
                                } else {
                                    context.fail(cause);
                                }
                                return;
                            }
                            context.session().put(ST.currentUser, r.result().body());
                            context.response().end(ST.ok);
                        });
            });
        });
    }
}
