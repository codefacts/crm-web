package io.crm.web.controller;

import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.excpt.ApiServiceException;
import io.crm.web.service.ApiService;
import io.vertx.ext.web.Router;

/**
 * Created by someone on 22/09/2015.
 */
final public class LoginController {
    private final ApiService apiService;
    private final Router router;
    public LoginController(final ApiService apiService, Router router) {
        this.apiService = apiService;
        this.router = router;
    }

    public void login() {
        router.post(Uris.login.value).handler(context -> {
            context.request().setExpectMultipart(true);
            context.request().endHandler(b -> {
                String username = context.request().formAttributes().get("username");
                String password = context.request().formAttributes().get("password");
                apiService.loginApi(username, password, r -> {
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
                    context.session().put(ST.currentUser, r.result());
                    context.response().end(ST.ok);
                });
            });
        });
    }
}
