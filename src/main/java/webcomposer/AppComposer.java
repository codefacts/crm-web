package webcomposer;

import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateMachine;
import io.crm.util.ExceptionUtil;
import io.crm.util.Util;
import io.crm.web.util.WebUtils;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.List;
import java.util.Map;

/**
 * Created by shahadat on 5/3/16.
 */
final public class AppComposer {

    private static final String BODY = "body";
    private static final String HEADER = "header";
    private static final CharSequence APPLICATION_JSON = "application/json; charset=utf-8";
    final Map<DomainInfo, Map<String, StateMachine>> stateMachinesByDomainInfo;
    final Router router;
    final List<DomainInfo> domainInfoList;
    final JDBCClient jdbcClient;
    final EventBus eventBus;

    AppComposer(Map<DomainInfo, Map<String, StateMachine>> stateMachinesByDomainInfo, Router router, List<DomainInfo> domainInfoList, JDBCClient jdbcClient, EventBus eventBus) {
        this.stateMachinesByDomainInfo = stateMachinesByDomainInfo;
        this.router = router;
        this.domainInfoList = domainInfoList;
        this.jdbcClient = jdbcClient;
        this.eventBus = eventBus;
    }

    public void create() {

        domainInfoList.forEach(domainInfo -> {

            registerEventHandler(domainInfo);

            registerControllers(domainInfo);
        });
    }

    private void registerControllers(DomainInfo domainInfo) {

        router.get(single(domainInfo.uri)).handler(BodyHandler.create());
        router.get(single(domainInfo.uri)).handler(ctx -> {

            controller(ctx, domainInfo, Events.FIND);
        });

        router.get(domainInfo.uri).handler(BodyHandler.create());
        router.get(domainInfo.uri).handler(ctx -> {

            controller(ctx, domainInfo, Events.FIND_ALL);
        });

        router.post(domainInfo.uri).handler(BodyHandler.create());
        router.post(domainInfo.uri).handler(ctx -> {

            controller(ctx, domainInfo, Events.CREATE);
        });

        router.put(single(domainInfo.uri)).handler(BodyHandler.create());
        router.put(single(domainInfo.uri)).handler(ctx -> {

            controller(ctx, domainInfo, Events.UPDATE);

        });

        router.delete(single(domainInfo.uri)).handler(BodyHandler.create());
        router.delete(single(domainInfo.uri)).handler(ctx -> {

            controller(ctx, domainInfo, Events.DELETE);
        });
    }

    private void registerEventHandler(DomainInfo domainInfo) {

        handler(domainInfo, Events.CREATE);

        handler(domainInfo, Events.FIND);

        handler(domainInfo, Events.FIND_ALL);

        handler(domainInfo, Events.UPDATE);

        handler(domainInfo, Events.DELETE);
    }

    private void controller(RoutingContext ctx, DomainInfo domainInfo, String action) {

        final JsonObject json = ctx.getBody().length() <= 0 ? new JsonObject() : ctx.getBodyAsJson();

        final DeliveryOptions options = new DeliveryOptions().setHeaders(ctx.request().params());

        Util.send(eventBus, address(domainInfo.address, action), json, options)
            .then(message -> {
                final Object body = message.body();
                if (body instanceof Json) {
                    ctx.response().putHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);
                    ctx.response().end(Json.encodePrettily(body));
                } else {
                    ctx.response().end(body.toString());
                }
            })
            .error(ctx::fail)
        ;
    }

    private String single(String uri) {
        return uri + "/:id";
    }

    private String address(String address, String action) {
        return address + "/" + action;
    }

    private void handler(DomainInfo domainInfo, String eventKey) {
        eventBus.consumer(address(domainInfo.address, eventKey),
            message -> WebUtils.executeSql(jdbcClient,
                connection -> stateMachine(domainInfo, connection, message, eventKey))
                .error(e -> ExceptionUtil.fail(message, e)));
    }

    private <R> Promise<R> stateMachine(DomainInfo domainInfo, SQLConnection connection, Message<Object> message, String eventKey) {

        final MSG<Object> msg = new MSGBuilder<>()
            .setBody(message.body())
            .setMessage(message)
            .setConnection(connection)
            .build();

        final StateMachine stateMachine = stateMachinesByDomainInfo.get(domainInfo).get(eventKey);

        return stateMachine.start(msg);
    }

    public static class Events {
        public static final String FIND = "find";
        public static final String FIND_ALL = "find-all";
        public static final String CREATE = "create";
        public static final String UPDATE = "update";
        public static final String DELETE = "delete";
    }
}
