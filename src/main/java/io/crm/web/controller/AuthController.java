package io.crm.web.controller;

import io.crm.promise.Promises;
import io.crm.promise.intfs.Promise;
import io.crm.util.Util;
import io.crm.web.ApiEvents;
import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.util.WebUtils;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.ext.web.handler.BodyHandler;

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
//        login(router);
//        logout(router);
//        sessionCount(router);
    }

    public void login(final Router router) {
        router.post(Uris.LOGIN.value).handler(BodyHandler.create());
        router.post(Uris.LOGIN.value).handler(ctx -> Promises.from()
            .mapToPromise(v -> Util.<JsonObject>send(vertx.eventBus(), ApiEvents.LOGIN_API,
                WebUtils.toJson(ctx.request().params())))
            .map(m -> m.body())
            .mapToPromise(user -> login(user, ctx, vertx))
            .error(ctx::fail));
    }

    public static void setSessionMonitorTimer(final Session session, Vertx vertx) {
        vertx.setTimer(5 * 60_000, event -> {
            if (!session.isDestroyed()) CURRENT_USER_COUNT.decrementAndGet();
            else setSessionMonitorTimer(session, vertx);
        });
    }

    public static void logout(final Router router) {
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

    public void sessionCount(final Router router) {
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

    public static Promise<JsonObject> login(JsonObject userObject, RoutingContext ctx, Vertx vertx) {
        return Promises.from(userObject)
            .then(user -> {
                ctx.session().put(ST.currentUser, user);
                ctx.response().end(ST.ok);
            })
            .then(val -> {
                LOGIN_COUNT.incrementAndGet();
                CURRENT_USER_COUNT.incrementAndGet();
                setSessionMonitorTimer(ctx.session(), vertx);
            });
    }
}
