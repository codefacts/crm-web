package io.crm.web.service.callreview;

import io.crm.QC;
import io.crm.model.User;
import io.crm.util.AsyncUtil;
import io.crm.util.ExceptionUtil;
import io.crm.web.ST;
import io.crm.web.excpt.ApiServiceException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.AsyncResultHandler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;

/**
 * Created by someone on 22/09/2015.
 */
public class ApiService {
    private final String apiBaseUri = "";
    private final int apiPort = 3276;
    private final String apiHost = "localhost";
    private final HttpClient httpClient;

    public ApiService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void loginApi(Message<JsonObject> m) {

        httpClient
                .post(apiPort, apiHost, apiBaseUri + "/login/apilogin")
                .handler(res -> {
                    res.bodyHandler(b -> {
                        if (res.statusCode() == HttpResponseStatus.OK.code()) {
                            final JsonObject jsonObject = new JsonObject(b.toString());
                            System.out.println("Logged in: " + jsonObject.encodePrettily());
                            m.reply(transformUser(jsonObject));
                        } else {
                            m.fail(res.statusCode(), b.toString());
                        }
                    }).exceptionHandler(e -> ExceptionUtil.fail(m, e));
                })
                .exceptionHandler(e -> ExceptionUtil.fail(m, e))
                .end(
                        new JsonObject()
                                .put(ST.username, m.body().getString(ST.username))
                                .put(ST.password, m.body().getString(ST.password))
                                .encode());
    }

    private <T> JsonObject transformUser(final JsonObject jsonObject) {
        return
                new JsonObject()
                        .put(QC.username, jsonObject.getString(ST.userName))
                        .put(QC.userId, jsonObject.getInteger(ST.userId) + "")
                        .put(User.mobile, jsonObject.getString(ST.mobile))
                        .put(QC.userType,
                                new JsonObject()
                                        .put(QC.id, jsonObject.getInteger(ST.userType))
                                        .put(QC.name, jsonObject.getString(ST.typeName)))
                ;
    }
}
