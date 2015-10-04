package io.crm.web.service.callreview;

import io.crm.util.ExceptionUtil;
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
    public static final String apiBaseUri = "";
    public static final int apiPort = 45707;
    public static final String apiHost = "localhost";
    private final HttpClient httpClient;

    public BrCheckerDetailsService(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void brCheckerData(final Message<JsonObject> message) {
        withReplyRun(() -> {
            Integer page = message.body().getInteger(ST.page, 1);
            Integer size = message.body().getInteger(ST.size, 20);
            message.reply(
                    new JsonObject()
                            .put(ST.data,
                                    new JsonArray()
                                            .add(
                                                    new JsonObject()
                                                            .put("name", "kona")
                                                            .put("jsdhjhksfghksdgklsjhgfkhskjghksljbfgksjhgksfgkljshfkgljhsklfgjhsklfgilshdgkljhsklfdghskjghksjhfgkljshfdklgjhsklgfbkslbfgklsjbgfklbskljfbgkslfbgksljbfgklsbgkf", "accountant")
                                            )
                                            .add(
                                                    new JsonObject()
                                                            .put("name", "kona")
                                                            .put("jsdhjhksfghksdgklsjhgfkhskjghksljbfgksjhgksfgkljshfkgljhsklfgjhsklfgilshdgkljhsklfdghskjghksjhfgkljshfdklgjhsklgfbkslbfgklsjbgfklbskljfbgkslfbgksljbfgklsbgkf", "accountant")
                                            )
                                            .add(
                                                    new JsonObject()
                                                            .put("name", "kona")
                                                            .put("jsdhjhksfghksdgklsjhgfkhskjghksljbfgksjhgksfgkljshfkgljhsklfgjhsklfgilshdgkljhsklfdghskjghksjhfgkljshfdklgjhsklgfbkslbfgklsjbgfklbskljfbgkslfbgksljbfgklsbgkf", "accountant")
                                            )
                                            .add(
                                                    new JsonObject()
                                                            .put("name", "kona")
                                                            .put("jsdhjhksfghksdgklsjhgfkhskjghksljbfgksjhgksfgkljshfkgljhsklfgjhsklfgilshdgkljhsklfdghskjghksjhfgkljshfdklgjhsklgfbkslbfgklsjbgfklbskljfbgkslfbgksljbfgklsbgkf", "accountant")
                                            )
                                            .add(
                                                    new JsonObject()
                                                            .put("name", "kona")
                                                            .put("jsdhjhksfghksdgklsjhgfkhskjghksljbfgksjhgksfgkljshfkgljhsklfgjhsklfgilshdgkljhsklfdghskjghksjhfgkljshfdklgjhsklgfbkslbfgklsjbgfklbskljfbgkslfbgksljbfgklsbgkf", "accountant")
                                            )
                                            .add(
                                                    new JsonObject()
                                                            .put("name", "kona")
                                                            .put("jsdhjhksfghksdgklsjhgfkhskjghksljbfgksjhgksfgkljshfkgljhsklfgjhsklfgilshdgkljhsklfdghskjghksjhfgkljshfdklgjhsklgfbkslbfgklsjbgfklbskljfbgkslfbgksljbfgklsbgkf", "accountant")
                                            )
                                            .add(
                                                    new JsonObject()
                                                            .put("name", "kona")
                                                            .put("jsdhjhksfghksdgklsjhgfkhskjghksljbfgksjhgksfgkljshfkgljhsklfgjhsklfgilshdgkljhsklfdghskjghksjhfgkljshfdklgjhsklgfbkslbfgklsjbgfklbskljfbgkslfbgksljbfgklsbgkf", "accountant")
                                            )
                                            .add(
                                                    new JsonObject()
                                                            .put("name", "kona")
                                                            .put("jsdhjhksfghksdgklsjhgfkhskjghksljbfgksjhgksfgkljshfkgljhsklfgjhsklfgilshdgkljhsklfdghskjghksjhfgkljshfdklgjhsklgfbkslbfgklsjbgfklbskljfbgkslfbgksljbfgklsbgkf", "accountant")
                                            )
                                            .add(
                                                    new JsonObject()
                                                            .put("name", "kona")
                                                            .put("jsdhjhksfghksdgklsjhgfkhskjghksljbfgksjhgksfgkljshfkgljhsklfgjhsklfgilshdgkljhsklfdghskjghksjhfgkljshfdklgjhsklgfbkslbfgklsjbgfklbskljfbgkslfbgksljbfgklsbgkf", "accountant")
                                            )
                                            .add(
                                                    new JsonObject()
                                                            .put("name", "kona")
                                                            .put("jsdhjhksfghksdgklsjhgfkhskjghksljbfgksjhgksfgkljshfkgljhsklfgjhsklfgilshdgkljhsklfdghskjghksjhfgkljshfdklgjhsklgfbkslbfgklsjbgfklbskljfbgkslfbgksljbfgklsbgkf", "accountant")
                                            )
                                            .add(
                                                    new JsonObject()
                                                            .put("name", "kona")
                                                            .put("jsdhjhksfghksdgklsjhgfkhskjghksljbfgksjhgksfgkljshfkgljhsklfgjhsklfgilshdgkljhsklfdghskjghksjhfgkljshfdklgjhsklgfbkslbfgklsjbgfklbskljfbgkslfbgksljbfgklsbgkf", "azkdlzksdz'ksdf';kadsfkjsdf'asdkjf'laksndfl'askdflaksdfla'skjdflaskndflas;kdnflaskndfls;kdnglskndfglskndfccountant")
                                                            .put("name2", "kona")
                                                            .put("designation2", "azkdlzksdz'ksdf';kadsfkjsdf'asdkjf'laksndfl'askdflaksdfla'skjdflaskndflas;kdnflaskndfls;kdnglskndfglskndfccountant")
                                                            .put("name3", "kona")
                                                            .put("designation3", "azkdlzksdz'ksdf';kadsfkjsdf'asdkjf'laksndfl'askdflaksdfla'skjdflaskndflas;kdnflaskndfls;kdnglskndfglskndfccountant")
                                                            .put("name4", "kona")
                                                            .put("designation4", "azkdlzksdz'ksdf';kadsfkjsdf'asdkjf'laksndfl'askdflaksdfla'skjdflaskndflas;kdnflaskndfls;kdnglskndfglskndfccountant")
                                                            .put("name", "kona")
                                                            .put("jsdhjhksfghksdgklsjhgfkhskjghksljbfgksjhgksfgkljshfkgljhsklfgjhsklfgilshdgkljhsklfdghskjghksjhfgkljshfdklgjhsklgfbkslbfgklsjbgfklbskljfbgkslfbgksljbfgklsbgkf", "azkdlzksdz'ksdf';kadsfkjsdf'asdkjf'laksndfl'askdflaksdfla'skjdflaskndflas;kdnflaskndfls;kdnglskndfglskndfccountant")
                                                            .put("name", "kona")
                                                            .put("jsdhjhksfghksdgklsjhgfkhskjghksljbfgksjhgksfgkljshfkgljhsklfgjhsklfgilshdgkljhsklfdghskjghksjhfgkljshfdklgjhsklgfbkslbfgklsjbgfklbskljfbgkslfbgksljbfgklsbgkf", "azkdlzksdz'ksdf';kadsfkjsdf'asdkjf'laksndfl'askdflaksdfla'skjdflaskndflas;kdnflaskndfls;kdnglskndfglskndfccountant")
                                            )
                            )
                            .put(ST.pagination,
                                    new JsonObject()
                                            .put(ST.page, page)
                                            .put(ST.size, size)
                                            .put(ST.total, 2000)
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
        }, message);
    }
}
