package io.crm.web.service.callreview;

import io.crm.QC;
import io.crm.model.User;
import io.crm.util.ExceptionUtil;
import io.crm.web.App;
import io.crm.web.WebST;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;

/**
 * Created by someone on 22/09/2015.
 */
public class ApiService {
    private final int apiPort = App.loadConfig().getJsonObject(ApiService.class.getSimpleName()).getInteger("apiPort");
    private final String apiHost = App.loadConfig().getJsonObject(ApiService.class.getSimpleName()).getString("apiHost");
    private final HttpClient httpClient;

    public ApiService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void loginApi(Message<JsonObject> m) {

        httpClient
                .post(apiPort, apiHost, "/login/apilogin")
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
                                .put(WebST.username, m.body().getString(WebST.username))
                                .put(WebST.password, m.body().getString(WebST.password))
                                .encode());
    }

    private <T> JsonObject transformUser(final JsonObject jsonObject) {
        return
                new JsonObject()
                        .put(QC.username, jsonObject.getString(WebST.userName))
                        .put(QC.userId, jsonObject.getInteger(WebST.userId) + "")
                        .put(User.mobile, jsonObject.getString(WebST.mobile))
                        .put(QC.userType,
                                new JsonObject()
                                        .put(QC.id, jsonObject.getInteger(WebST.userType))
                                        .put(QC.name, jsonObject.getString(WebST.typeName)))
                ;
    }
}
