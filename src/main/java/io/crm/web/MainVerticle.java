package io.crm.web;

import io.crm.web.view.EventPublisherForm;
import io.crm.web.view.Page;
import io.crm.web.view.PageBuilder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

import static io.crm.util.Util.isEmptyOrNullOrSpaces;
import static io.crm.util.Util.listEvents;

/**
 * Created by someone on 07/09/2015.
 */
public class MainVerticle extends AbstractVerticle {
    private final String templatesDir = "D:\\IdeaProjects\\crm-web\\src\\main\\resources\\templates\\";

    @Override
    public void start() throws Exception {
        final Router router = Router.router(vertx);
        registerRequestHandlers(router);
        getVertx().createHttpServer().requestHandler(router::accept).listen(8085);
        System.out.println("<----------------------------------WEB_SERVER_STARTED------------------------------------->");
    }

    private void registerRequestHandlers(final Router router) {
        registerStaticFileHandlers(router);

        router.get("/event-publish-form").handler(context -> {
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
        router.route("/static/*").handler(StaticHandler.create("D:\\IdeaProjects\\crm-web\\src\\main\\resources\\static\\"));
    }
}
