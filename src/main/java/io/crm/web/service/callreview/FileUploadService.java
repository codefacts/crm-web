package io.crm.web.service.callreview;

import io.crm.FailureCode;
import io.crm.util.ExceptionUtil;
import io.crm.web.ST;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static io.crm.util.ExceptionUtil.withHandler;
import static io.crm.util.ExceptionUtil.withReplyRun;
import static java.lang.Integer.parseInt;

/**
 * Created by someone on 02/10/2015.
 */
public class FileUploadService {
    final HttpClient httpClient;

    public FileUploadService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void uploadBrCheckerData(final Message<JsonObject> message) {
        withReplyRun(() -> {
            httpClient
                    .post(BrCheckerDetailsService.apiPort, BrCheckerDetailsService.apiHost, String.format(
                            "/Import/importFile?file=%s&extention=%s",
                            URLEncoder.encode(message.body().getString(ST.file), StandardCharsets.UTF_8.name()),
                            URLEncoder.encode(message.body().getString(ST.extention), StandardCharsets.UTF_8.name())
                    ))
                    .setTimeout(30 * 60 * 1000)
                    .exceptionHandler(e -> ExceptionUtil.fail(message, e))
                    .handler(withHandler(res -> {
                        res
                                .bodyHandler(withHandler(b -> {

                                    if (res.statusCode() == HttpResponseStatus.OK.code()) {
                                        message.reply(
                                                new JsonObject(b.toString())
                                                        .getInteger(ST.count));

                                    } else if (res.statusCode() == HttpResponseStatus.BAD_REQUEST.code()) {
                                        message.fail(FailureCode.BadRequest.code, b.toString());

                                    } else {
                                        message.fail(FailureCode.UnknownError.code, FailureCode.UnknownError.message + "[ " + b.toString() + " ]");
                                    }
                                }, message))
                                .exceptionHandler(e -> ExceptionUtil.fail(message, e))
                        ;
                    }, message))
                    .setChunked(true)
                    .end();

            //        message.reply(123);
//            message.fail(FailureCode.BadRequest.code,
//                    new JsonObject()
//                            .put("Line 45", "Invalid Name")
//                            .encode());
        }, message);
    }

    public static void main(String... args) {
        System.out.println(StandardCharsets.UTF_8.name());
    }
}
