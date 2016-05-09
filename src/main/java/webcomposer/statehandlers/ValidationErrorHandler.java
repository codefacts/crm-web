package webcomposer.statehandlers;

import io.crm.ErrorCodes;
import io.crm.MessageBundle;
import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateCallbacksBuilder;
import io.crm.statemachine.StateMachine;
import io.crm.statemachine.StateTrigger;
import io.crm.validator.ValidationResult;
import io.vertx.core.json.JsonObject;
import webcomposer.MSG;
import webcomposer.Rsp;
import webcomposer.util.EventCn;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by shahadat on 5/8/16.
 */
public class ValidationErrorHandler {
    public static final String VALIDATION_RESULTS = "validationResults";
    private final MessageBundle messageBundle;

    public ValidationErrorHandler(MessageBundle messageBundle) {
        this.messageBundle = messageBundle;
    }

    private FunctionUnchecked
        <MSG<List<ValidationResult>>,
            Promise
                <StateTrigger
                    <MSG<JsonObject>>>>
    enter() {
        return listMSG -> {

            final List<JsonObject> list = listMSG.body.stream().map(ValidationResult::toJson).collect(Collectors.toList());

            return Promises.from(
                StateMachine.trigger(EventCn.REPORT_ERROR,
                    listMSG.<JsonObject>builder()
                        .setBody(
                            new JsonObject()
                                .put(Rsp.CODE, ErrorCodes.VALIDATION_ERROR.code())
                                .put(Rsp.MESSAGE_CODE, ErrorCodes.VALIDATION_ERROR.messageCode())
                                .put(
                                    Rsp.MESSAGE, messageBundle.translate(ErrorCodes.VALIDATION_ERROR.messageCode(),
                                        new JsonObject()
                                            .put(VALIDATION_RESULTS, list)))
                        ).build()));
        };
    }

    public StateCallbacks<MSG<List<ValidationResult>>, MSG<JsonObject>> toStateCallbacks() {
        return new StateCallbacksBuilder<MSG<List<ValidationResult>>, MSG<JsonObject>>()
            .onEnter(enter()).build();
    }
}
