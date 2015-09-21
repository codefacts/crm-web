package io.crm.web;

import io.crm.Events;
import io.crm.QC;
import io.crm.util.AsyncUtil;
import io.crm.web.excpt.ApiServiceException;
import io.crm.web.view.EventPublisherForm;
import io.crm.web.view.LoginTemplate;
import io.crm.web.view.Page;
import io.crm.web.view.PageBuilder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.AsyncResultHandler;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.sstore.SessionStore;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static io.crm.util.Util.isEmptyOrNullOrSpaces;
import static io.crm.util.Util.listEvents;
import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;
import static io.vertx.core.http.HttpHeaders.TEXT_HTML;

/**
 * Created by someone on 07/09/2015.
 */
public class MainVerticle extends AbstractVerticle {
    private final String templatesDir = "D:\\IdeaProjects\\crm-web\\src\\main\\resources\\templates\\";
    private final String apiBaseUri = "http://localhost:3276";
    private final int apiPort = 3276;
    private final String apiHost = "localhost";
    private HttpClient httpClient;

    //
    @Override
    public void start() throws Exception {
        httpClient = vertx.createHttpClient();
        System.setProperty("dev-mode", "true");

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
                Uris.staticResources.value,
                Uris.publicResources.value,
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

        eventPublishForm(router);

        homeController(router);

        loginFormController(router);
        loginController(router);
        logoutController(router);
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

    private void loginController(final Router router) {
        router.post(Uris.login.value).handler(context -> {
            context.request().setExpectMultipart(true);
            context.request().endHandler(b -> {
                String username = context.request().formAttributes().get("username");
                String password = context.request().formAttributes().get("password");
                loginApi(username, password, r -> {
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

    private void logoutController(final Router router) {
        router.get(Uris.logout.value).handler(context -> {
            context.session().destroy();
            context.response().end(ST.ok);
        });
    }

    private void loginApi(final String username, final String password, final AsyncResultHandler<JsonObject> handler) {
        httpClient
                .post(apiPort, apiHost, apiBaseUri + "/login/apilogin")
                .handler(event -> {
                    event.bodyHandler(b -> {
                        if (event.statusCode() == HttpResponseStatus.OK.code()) {
                            final JsonObject jsonObject = new JsonObject(b.toString());
                            handler.handle(AsyncUtil.success(jsonObject));
                        } else {
                            handler.handle(AsyncUtil.fail(new ApiServiceException(event.statusCode(), b.toString())));
                        }
                    }).exceptionHandler(e -> handler.handle(AsyncUtil.fail(e)));
                })
                .exceptionHandler(e -> handler.handle(AsyncUtil.fail(e)))
                .end(
                        new JsonObject()
                                .put(ST.username, username)
                                .put(ST.password, password)
                                .encode());
    }

    private void homeController(final Router router) {
        router.get(Uris.home.value).handler(context -> {
            context.response().headers().set(CONTENT_TYPE, "text/html");
            context.response().end("Home");
        });
    }

    private void eventPublishForm(final Router router) {
        router.get(Uris.event_publish_form.value).handler(context -> {
            if (context.failed()) {
                return;
            }
            final String destination = context.request().params().get("destination");
            final String body = context.request().params().get("body");
            final String header = context.request().params().get("header");

            final JsonObject headerJson = isEmptyOrNullOrSpaces(header) ? null : new JsonObject(header);
            final JsonObject bodyJson = isEmptyOrNullOrSpaces(body) ? null : new JsonObject(body);
            final DeliveryOptions deliveryOptions = headerJson == null ? new DeliveryOptions() : new DeliveryOptions(new JsonObject().put("header", headerJson));

            if (isEmptyOrNullOrSpaces(destination)) {
                final Page page = PageBuilder.create("Publish Event")
                        .body(new EventPublisherForm(listEvents(), destination, header, body, "Invalid input. Destination can't be empty."))
                        .build();
                context.response().end(page.render());
                return;
            }

            vertx.eventBus().send(destination, bodyJson, deliveryOptions, (AsyncResult<Message<Object>> r1) -> {
                if (r1.failed()) {
                    final Page page = PageBuilder.create("Publish Event")
                            .body(new EventPublisherForm(listEvents(), destination, header, body, r1.cause().getClass() + " : " + r1.cause().getMessage()))
                            .build();
                    context.response().end(page.render());
                    return;
                }
                final Object reply = r1.result().body();
                final Page page = PageBuilder.create("Publish Event")
                        .body(new EventPublisherForm(listEvents(), destination, header, body, reply instanceof JsonObject ?
                                ((JsonObject) reply).encodePrettily() : ((JsonArray) reply).encodePrettily()))
                        .build();
                context.response().end(page.render());
            });
        });
    }

    private void registerStaticFileHandlers(final Router router) {
        router.route(Uris.staticResources.value).handler(StaticHandler.create("D:\\IdeaProjects\\crm-web\\src\\main\\resources\\static\\"));
        router.route(Uris.publicResources.value).handler(StaticHandler.create("D:\\IdeaProjects\\crm-web\\src\\main\\resources\\public\\"));
    }
}
