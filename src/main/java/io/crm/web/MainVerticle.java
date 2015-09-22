package io.crm.web;

import io.crm.web.controller.EventPublisherController;
import io.crm.web.controller.HomeController;
import io.crm.web.controller.LoginController;
import io.crm.web.service.ApiService;
import io.crm.web.view.*;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;
import static io.vertx.core.http.HttpHeaders.TEXT_HTML;

/**
 * Created by someone on 07/09/2015.
 */
public class MainVerticle extends AbstractVerticle {
    private final String templatesDir = "D:\\IdeaProjects\\crm-web\\src\\main\\resources\\templates\\";
    private ApiService apiService;
    private HttpClient httpClient;

    //
    @Override
    public void start() throws Exception {
        httpClient = vertx.createHttpClient();
        System.setProperty("dev-mode", "true");
        apiService = new ApiService(httpClient);

        //Configure Router
        final Router router = Router.router(vertx);
        router.route().handler(CookieHandler.create());
        SessionStore store = LocalSessionStore.create(vertx);
        SessionHandler sessionHandler = SessionHandler.create(store);
        router.route().handler(sessionHandler);

        //Register Listeners
        registerFilters(router);
        registerRequestHandlers(router);
        getVertx().createHttpServer().requestHandler(router::accept).listen(8085);
        System.out.println("<----------------------------------WEB_SERVER_STARTED------------------------------------->");
    }

    private void registerFilters(final Router router) {
        corsFilter(router);
        noCacheFilter(router);
        authFilter(router);
    }

    private void corsFilter(final Router router) {
        router.route().handler(context -> {
            context.response().headers()
                    .set(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                    .set(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST, GET, OPTIONS, DELETE")
                    .set(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "3600")
                    .set(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "x-requested-with")
            ;
            context.next();
        });
    }

    private void noCacheFilter(final Router router) {
        router.route().handler(context -> {
            if (!context.request().uri().startsWith("/static")) {
                context.response().headers().set(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
                context.response().headers().set("Pragma", "no-cache");
                context.response().headers().set(HttpHeaders.EXPIRES, "0");
            }
            context.next();
        });
    }

    private void authFilter(final Router router) {
        Set<String> publicUris = Arrays.asList(
                Uris.staticResourcesPattern.value,
                Uris.publicResourcesPattern.value,
                Uris.login.value,
                Uris.register.value,
                Uris.event_publish_form.value
        )
                .stream()
                .map(uri -> {
                    final int index = uri.lastIndexOf('/');
                    if (index > 0) {
                        return uri.substring(0, index);
                    }
                    return uri;
                })
                .collect(Collectors.toSet());
        System.out.println("publicUris: " + publicUris);

        router.route().handler(context -> {
            if (context.session().get(ST.currentUser) != null) {
                context.next();
                return;
            }
            final String uri = context.request().uri();

            if (publicUris.stream().filter(publicUri -> uri.startsWith(publicUri)).findAny().isPresent()) {
                context.next();
                return;
            }
            System.out.println("Redirecting : " + uri);
            context.response().setStatusCode(HttpResponseStatus.TEMPORARY_REDIRECT.code());
            context.response().headers().set(HttpHeaders.LOCATION, Uris.login.value);
            context.response().end();
        });
    }

    private void registerRequestHandlers(final Router router) {
        registerStaticFileHandlers(router);

        otherwiseController(router);

        new EventPublisherController(vertx).eventPublishForm(router);

        new HomeController(router).index();

        loginFormController(router);
        new LoginController(apiService, router).login();
        logoutController(router);
    }

    private void otherwiseController(final Router router) {
        router.get("/").handler(context -> {
            if (WebUtils.isLoggedIn(context.session())) {
                WebUtils.redirect(Uris.dashboard.value, context.response());
            } else {
                WebUtils.redirect(Uris.login.value, context.response());
            }
        });
    }

    private void loginFormController(final Router router) {
        router.get(Uris.login.value).handler(context -> {
            context.response().headers().set(CONTENT_TYPE, TEXT_HTML);

            context.response().end(
                    new PageBuilder("Login")
                            .body(new LoginTemplate())
                            .build().render());
        });
    }

    private void logoutController(final Router router) {
        router.get(Uris.logout.value).handler(context -> {
            context.session().destroy();
            WebUtils.redirect(Uris.login.value, context.response());
        });
    }

    private void registerStaticFileHandlers(final Router router) {
        router.route(Uris.staticResourcesPattern.value).handler(StaticHandler.create("D:\\IdeaProjects\\crm-web\\src\\main\\resources\\static\\"));
        router.route(Uris.publicResourcesPattern.value).handler(StaticHandler.create("D:\\IdeaProjects\\crm-web\\src\\main\\resources\\public\\"));
    }
}
