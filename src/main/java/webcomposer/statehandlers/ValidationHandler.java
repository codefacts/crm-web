package webcomposer.statehandlers;

import com.google.common.collect.ImmutableList;
import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateCallbacksBuilder;
import io.crm.statemachine.StateMachine;
import io.crm.statemachine.StateTrigger;
import io.crm.validator.ValidationPipeline;
import io.crm.validator.ValidationPipelineDeferred;
import io.crm.validator.ValidationResult;
import io.vertx.core.json.JsonObject;
import webcomposer.MSG;
import webcomposer.util.EventCn;

import java.util.Collections;
import java.util.List;

/**
 * Created by shahadat on 5/8/16.
 */
public class ValidationHandler {

    final ValidationPipeline<JsonObject> validationPipeline;
    final ValidationPipelineDeferred validationPipelineDeferred;

    public ValidationHandler(ValidationPipeline<JsonObject> validationPipeline, ValidationPipelineDeferred validationPipelineDeferred) {
        this.validationPipeline = validationPipeline == null ? validationPipeline : validators();
        this.validationPipelineDeferred = validationPipelineDeferred == null ? new ValidationPipelineDeferred(ImmutableList.of()) : validationPipelineDeferred;
    }

    private ValidationPipeline<JsonObject> validators() {
        return new ValidationPipeline<>(Collections.emptyList());
    }

    private FunctionUnchecked
        <MSG<JsonObject>,
            Promise
                <StateTrigger
                    <MSG<Object>>>> enter() {
        return msg -> {

            {
                final List<ValidationResult> validationResults = validationPipeline.validate(msg.body);

                if (validationResults == null) {
                    return Promises.from(
                        StateMachine.trigger(EventCn.VALIDATION_FAILED,
                            msg.builder().setBody(validationResults).build()));
                }
            }

            return validationPipelineDeferred.validate(msg.body, msg.store)

                .mapToPromise(validationResults -> {

                    if (validationResults == null) {
                        return Promises.from(
                            StateMachine.trigger(EventCn.VALIDATION_FAILED,
                                msg.builder().setBody(validationResults).build()));
                    }

                    return null;
                })
                ;
        };
    }

    public StateCallbacks<MSG<JsonObject>, MSG<Object>> toStateCallbacks() {
        return new StateCallbacksBuilder<MSG<JsonObject>, MSG<Object>>()
            .onEnter(enter()).build();
    }
}
