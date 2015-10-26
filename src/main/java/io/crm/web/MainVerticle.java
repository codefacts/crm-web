package io.crm.web;

import io.crm.QC;
import io.crm.model.User;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Defer;
import io.crm.util.Util;
import io.crm.web.controller.*;
import io.crm.web.service.callreview.*;
import io.crm.web.template.*;
import io.crm.web.template.page.LoginTemplate;
import io.crm.web.util.WebUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
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

import static io.vertx.core.http.HttpHeaders.CONTENT_TYPE;
import static io.vertx.core.http.HttpHeaders.TEXT_HTML;

/**
 * Created by someone on 07/09/2015.
 */
final public class MainVerticle extends AbstractVerticle {
    private HttpClient httpClient;
    private final Set<String> publicUris = publicUris();
    public static final String PORT_PROPERTY = "PORT";
    public static final int PORT = App.loadConfig().getInteger(PORT_PROPERTY);

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
        getVertx().createHttpServer().requestHandler(router::accept).listen(PORT);
        System.out.println("<----------------------------------WEB_SERVER_STARTED------------------------------------->");
        System.out.println("PORT: " + PORT);
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
        vertx.eventBus().consumer(ApiEvents.CHECK_IF_ALREADY_UPLOADED_SUCCESSFULLY, new FileUploadHistoryService()::checkIfAlreadyUploadedSuccessfully);


        Promises.success()
                .success(ctx -> {

//                    brCheckerJsonService.initialize(vertx);
//                    vertx.eventBus().consumer(ApiEvents.SEARCH_CLUSTER, brCheckerJsonService::searchCluster);
//                    vertx.eventBus().consumer(ApiEvents.SEARCH_TSR_CODE, brCheckerJsonService::searchTSRCode);
//                    vertx.eventBus().consumer(ApiEvents.SEARCH_AUDITOR_CODE, brCheckerJsonService::searchAuditorCode);
//                    vertx.eventBus().consumer(ApiEvents.SEARCH_CONSUMER_NAME, brCheckerJsonService::searchConsumerName);
//                    vertx.eventBus().consumer(ApiEvents.SEARCH_CONSUMER_MOBILE, brCheckerJsonService::searchConsumerMobile);
//                    vertx.eventBus().consumer(ApiEvents.FIND_ALL_CALL_STATUSES, brCheckerJsonService::findAllCallStatuses);
                })
                .success(ctx -> System.out.println("++++++++++++++++++ APPLICATION READY +++++++++++++++++"))
        ;
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

        router.route().handler(context -> {
            if (System.getProperty("dev-mode") != null) {
                context.session().put(ST.currentUser,
                        new JsonObject()
                                .put(QC.username, "Sohan")
                                .put(QC.userId, "br-124")
                                .put(User.mobile, "01553661069")
                                .put(QC.userType,
                                        new JsonObject()
                                                .put(QC.id, 1)
                                                .put(QC.name, "Programmer")));
            }
            if (context.session().get(ST.currentUser) != null) {
                context.next();
                return;
            }
            final String uri = context.request().uri();

            if (publicUris.stream().filter(publicUri -> uri.startsWith(publicUri)).findAny().isPresent()) {
                context.next();
                return;
            }
            //Auth Failed
            if ("XMLHttpRequest".equalsIgnoreCase(context.request().headers().get("X-Requested-With"))) {
                context.response().setStatusCode(HttpResponseStatus.FORBIDDEN.code())
                        .end("Please login to authorize your request.");
                return;
            }
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

        new BrCheckerJsonController(vertx, router);

        loginFormController(router);
        new LoginController(vertx, router).login();
        logoutController(router);

        new TestController().testController(router);
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
                        .setFilesReadOnly(true)
                        .setMaxAgeSeconds(3 * 30 * 24 * 60 * 60)
                        .setIncludeHidden(false)
                        .setEnableFSTuning(true)
        );
        router.route(Uris.publicResourcesPattern.value).handler(
                StaticHandler.create(App.PUBLIC_DIRECTORY)
                        .setFilesReadOnly(true)
                        .setMaxAgeSeconds(0)
                        .setIncludeHidden(false)
                        .setEnableFSTuning(true)
        );
    }

    private Set<String> publicUris() {
        return Arrays.asList(
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
    }

}
