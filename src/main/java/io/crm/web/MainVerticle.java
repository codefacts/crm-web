package io.crm.web;

import io.crm.QC;
import io.crm.model.User;
import io.crm.web.controller.*;
import io.crm.web.service.callreview.ApiService;
import io.crm.web.service.callreview.BrCheckerDetailsService;
import io.crm.web.service.callreview.FileUploadHistoryService;
import io.crm.web.service.callreview.FileUploadService;
import io.crm.web.template.*;
import io.crm.web.template.bootstrap.ModalAlertBuilder;
import io.crm.web.util.WebUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpHeaders;
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

import static io.crm.web.util.WebUtils.webHandler;
import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;
import static io.vertx.core.http.HttpHeaders.TEXT_HTML;

/**
 * Created by someone on 07/09/2015.
 */
final public class MainVerticle extends AbstractVerticle {
    private HttpClient httpClient;

    //
    @Override
    public void start() throws Exception {
        httpClient = vertx.createHttpClient();
        registerEvents();

        //Configure Router
        final Router router = Router.router(vertx);
        router.route().handler(CookieHandler.create());
        SessionStore store = LocalSessionStore.create(vertx);
        SessionHandler sessionHandler = SessionHandler.create(store);
        router.route().handler(sessionHandler);

        //Register Listeners
        registerFilters(router);
        registerControllers(router);
        int port = 8085;
        getVertx().createHttpServer().requestHandler(router::accept).listen(port);
        System.out.println("<----------------------------------WEB_SERVER_STARTED------------------------------------->");
        System.out.println("PORT: " + port);
    }

    private void registerEvents() {

        final ApiService apiService = new ApiService(httpClient);
        vertx.eventBus().consumer(ApiEvents.LOGIN_API, (Message<JsonObject> m) -> {

            m.reply(new JsonObject()
                    .put(QC.username, "Sohan")
                    .put(QC.userId, "br-124")
                    .put(User.mobile, "01553661069")
                    .put(QC.userType,
                            new JsonObject()
                                    .put(QC.id, 1)
                                    .put(QC.name, "Programmer")));
        });

        final FileUploadService fileUploadService = new FileUploadService(httpClient);
        vertx.eventBus().consumer(ApiEvents.UPLOAD_BR_CHECKER_DATA, fileUploadService::uploadBrCheckerData);

        BrCheckerDetailsService brCheckerDetailsService = new BrCheckerDetailsService(httpClient);
        vertx.eventBus().consumer(ApiEvents.BR_CHECKER_DETAILS, brCheckerDetailsService::brCheckerData);
        vertx.eventBus().consumer(ApiEvents.FIND_ONE_BR_CHECKER_INFO, brCheckerDetailsService::findOne);
        vertx.eventBus().consumer(ApiEvents.INSERT_BR_CHECKER_INFO, brCheckerDetailsService::insert);
        vertx.eventBus().consumer(ApiEvents.INSERT_FILE_UPLOADS_HISTORY, new FileUploadHistoryService()::insert);
        vertx.eventBus().consumer(ApiEvents.UPDATE_FILE_UPLOADS_HISTORY, new FileUploadHistoryService()::update);
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

    private void registerControllers(final Router router) {
        registerStaticFileHandlers(router);

        otherwiseController(router);

        new EventPublisherController(vertx).eventPublishForm(router);

        new HomeController(router).index();

        new CallController(vertx, router);

        new FileUploadController(vertx, router);

        new BrCheckerController(router, vertx);

        new ImageController(router);

        new GoogleMapController(router);

        new ImageUploadController(vertx, router);

        loginFormController(router);
        new LoginController(vertx, router).login();
        logoutController(router);

        testController(router);
    }

    private void testController(final Router router) {
        router.get("/test").handler(webHandler(ctx -> {
            ctx.response().end(
                    new PageBuilder("Test")
                            .body(
                                    new DashboardTemplateBuilder()
                                            .setUser(ctx.session().get(ST.currentUser))
                                            .setContentTemplate(
                                                    new ModalAlertBuilder()
                                                            .setTitle("Congrat")
                                                            .setBody("Hi")
                                                            .setShow(true)
                                                            .createModalAlert()
                                            )
                                            .build()
                            )
                            .build().render()
            );
        }));
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
            if (WebUtils.isLoggedIn(context.session())) {
                WebUtils.redirect(Uris.dashboard.value, context.response());
                return;
            }
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
        router.route(Uris.staticResourcesPattern.value).handler(
                StaticHandler.create(App.STATIC_DIRECTORY)
                        .setCachingEnabled(true)
                        .setEnableFSTuning(true)
        );
        router.route(Uris.publicResourcesPattern.value).handler(
                StaticHandler.create(App.PUBLIC_DIRECTORY));
    }
}
