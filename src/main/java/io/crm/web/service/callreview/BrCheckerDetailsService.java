package io.crm.web.service.callreview;

import io.crm.util.ExceptionUtil;
import io.crm.web.WebApp;
import io.crm.web.WebST;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;

import static io.crm.util.ExceptionUtil.withReply;
import static io.crm.util.ExceptionUtil.withReplyRun;

/**
 * Created by sohan on 10/3/2015.
 */
public class BrCheckerDetailsService {
    public static final int apiPort = WebApp.loadConfig().getJsonObject(BrCheckerDetailsService.class.getSimpleName(), new JsonObject()).getInteger("apiPort");
    public static final String apiHost = WebApp.loadConfig().getJsonObject(BrCheckerDetailsService.class.getSimpleName()).getString("apiHost");
    private final HttpClient httpClient;

    public BrCheckerDetailsService(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void insert(final Message<JsonObject> message) {
        withReply((JsonObject js) -> {
            message.reply(
                    new JsonObject()
                            .put(WebST.statusCode, WebST.success)
            );
        }, message);
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
            int page = message.body().getInteger(WebST.page, 1);
            int size = message.body().getInteger(WebST.size, 20);

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

    public static String baseUrl() {
        return "http://" + apiHost + ":" + apiPort;
    }
}
