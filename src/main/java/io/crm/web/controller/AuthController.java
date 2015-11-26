package io.crm.web.controller;

import io.crm.promise.Promises;
import io.crm.util.Util;
import io.crm.web.ApiEvents;
import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.util.WebUtils;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.Session;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by someone on 22/09/2015.
 */
final public class AuthController {
    public static final AtomicInteger LOGIN_COUNT = new AtomicInteger(0);
    public static final AtomicInteger LOGOUT_COUNT = new AtomicInteger(0);
    public static final AtomicInteger CURRENT_USER_COUNT = new AtomicInteger(0);
    private final Vertx vertx;

    public AuthController(final Vertx vertx, Router router) {
        this.vertx = vertx;
        login(router);
        logout(router);
        sessionCount(router);
    }

    public void login(final Router router) {
        router.post(Uris.LOGIN.value).handler(ctx -> {
            ctx.request().setExpectMultipart(true);
            ctx.request().endHandler(b -> {
                Promises.from()
                        .then(v -> {
                            String username = ctx.request().formAttributes().get("username");
                            String password = ctx.request().formAttributes().get("password");
                            Util.<JsonObject>send(vertx.eventBus(), ApiEvents.LOGIN_API,
                                    new JsonObject()
                                            .put(ST.username, username)
                                            .put(ST.password, password))
                                    .map(m -> m.body())
                                    .then(user -> {
                                        ctx.session().put(ST.currentUser, user);
                                        ctx.response().end(ST.ok);
                                    })
                                    .then(val -> {
                                        LOGIN_COUNT.incrementAndGet();
                                        CURRENT_USER_COUNT.incrementAndGet();
                                        setSessionMonitorTimer(ctx.session());
                                    })
                                    .error(ctx::fail)
                            ;
                        })
                ;
            });
        });
    }

    private void setSessionMonitorTimer(final Session session) {
        vertx.setTimer(5 * 60_000, event -> {
            if (!session.isDestroyed()) CURRENT_USER_COUNT.decrementAndGet();
            else setSessionMonitorTimer(session);
        });
    }

    private void logout(final Router router) {
        router.get(Uris.LOGOUT.value).handler(context -> {
            Promises.from()
                    .then(v -> {
                        context.session().destroy();
                        WebUtils.redirect(Uris.LOGIN.value, context.response());
                    })
                    .then(v -> {
                        LOGOUT_COUNT.incrementAndGet();
                        CURRENT_USER_COUNT.decrementAndGet();
                    })
                    .error(context::fail)
            ;
        });
    }

    private void sessionCount(final Router router) {
        router.get(Uris.SESSION_COUNT.value).handler(ctx -> {
            ctx.response().end(
                    new JsonObject()
                            .put("session-count", Util.toString(CURRENT_USER_COUNT.get()))
                            .put("login-count", Util.toString(LOGIN_COUNT.get()))
                            .put("logout-count", Util.toString(LOGOUT_COUNT.get()))
                            .encodePrettily()
            );
        });
    }
}
