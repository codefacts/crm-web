package io.crm.web.service.callreview;

import io.crm.util.ExceptionUtil;
import io.crm.web.App;
import io.crm.web.ST;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import static io.crm.util.ExceptionUtil.withReplyRun;

/**
 * Created by sohan on 10/3/2015.
 */
public class BrCheckerDetailsService {
    public static final int apiPort = App.loadConfig().getJsonObject(BrCheckerDetailsService.class.getSimpleName(), new JsonObject()).getInteger("apiPort");
    public static final String apiHost = App.loadConfig().getJsonObject(BrCheckerDetailsService.class.getSimpleName()).getString("apiHost");
    private final HttpClient httpClient;

    public BrCheckerDetailsService(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void findOne(final Message<Integer> message) {
        httpClient.get(apiPort, apiHost, "/BrChecker/findOne?id=" + message.body())
                .exceptionHandler(e -> ExceptionUtil.fail(message, e))
                .handler(res -> {
                    res
                            .bodyHandler(buffer -> {
                                message.reply(new JsonObject(buffer.toString()));
                            })
                            .exceptionHandler(e -> ExceptionUtil.fail(message, e))
                    ;
                })
                .end();
    }

    public void brCheckerData(final Message<JsonObject> message) {
        withReplyRun(() -> {
            int page = message.body().getInteger(ST.page, 1);
            int size = message.body().getInteger(ST.size, 20);

            httpClient
                    .get(apiPort, apiHost, String.format("/BrChecker/details?page=%d&size=%d", page, size))
                    .exceptionHandler(e -> ExceptionUtil.fail(message, e))
                    .handler(res -> {
                        res
                                .bodyHandler(buffer -> {
                                    message.reply(new JsonObject(buffer.toString()));
                                })
                                .exceptionHandler(e -> ExceptionUtil.fail(message, e))
                        ;
                    })
                    .end();
        }, message);
    }
}
