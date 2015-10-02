package io.crm.web.service.callreview;

import io.crm.FailureCode;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

/**
 * Created by someone on 02/10/2015.
 */
public class FileUploadService {
    public void uploadBrCheckerData(Message<String> message) {
//        message.reply(123);
        message.fail(FailureCode.BadRequest.code,
                new JsonObject()
                        .put("Line 45", "Invalid Name")
                        .encode());
    }
}
