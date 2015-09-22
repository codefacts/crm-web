package io.crm.web.service;

import io.crm.QC;
import io.crm.model.User;
import io.crm.util.AsyncUtil;
import io.crm.web.ST;
import io.crm.web.excpt.ApiServiceException;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResultHandler;
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

    public void loginApi(final String username, final String password, final AsyncResultHandler<JsonObject> handler) {
        httpClient
                .post(apiPort, apiHost, apiBaseUri + "/login/apilogin")
                .handler(event -> {
                    event.bodyHandler(b -> {
                        if (event.statusCode() == HttpResponseStatus.OK.code()) {
                            final JsonObject jsonObject = new JsonObject(b.toString());
                            System.out.println("Logged in: " + jsonObject.encodePrettily());
                            handler.handle(AsyncUtil.success(transformUser(jsonObject)));
                        } else {
                            handler.handle(AsyncUtil.fail(new ApiServiceException(event.statusCode(), b.toString())));
                        }
                    }).exceptionHandler(e -> handler.handle(AsyncUtil.fail(e)));
                })
                .exceptionHandler(e -> handler.handle(AsyncUtil.fail(e)))
                .end(
                        new JsonObject()
                                .put(ST.username, username)
                                .put(ST.password, password)
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
