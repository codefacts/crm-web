package io.crm.web.service.callreview;

import io.crm.util.ExceptionUtil;
import io.crm.web.ST;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * Created by sohan on 10/3/2015.
 */
public class BrCheckerDetailsService {
    private final String apiBaseUri = "";
    private final int apiPort = 3276;
    private final String apiHost = "localhost";
    private final HttpClient httpClient;

    public BrCheckerDetailsService(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void brCheckerData(final Message<JsonObject> message) {
        Integer page = message.body().getInteger(ST.page, 0);
        Integer size = message.body().getInteger(ST.size, 20);
        message.reply(
                new JsonObject()
                        .put(ST.data,
                                new JsonArray()
                                        .add(
                                                new JsonObject()
                                                        .put("name", "kona")
                                                        .put("designation", "accountant")
                                        ))
                        .put(ST.pagination,
                                new JsonObject()
                                        .put(ST.page, 1)
                                        .put(ST.size, 20)
                                        .put(ST.total, 2)
                        ));
//        httpClient
//                .get(apiPort, apiHost, String.format("/br-checker/details?page=%d&size=%d", page, size))
//                .exceptionHandler(e -> ExceptionUtil.fail(message, e))
//                .handler(res -> {
//                    res
//                            .bodyHandler(buffer -> {
//                                message.reply(new JsonArray(buffer.toString()));
//                            })
//                            .exceptionHandler(e -> ExceptionUtil.fail(message, e))
//                    ;
//                })
//                .end();
    }
}
