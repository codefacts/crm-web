package webcomposer.statehandlers;

import com.google.common.collect.ImmutableList;
import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
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
import java.util.concurrent.Callable;

/**
 * Created by shahadat on 5/8/16.
 */
public class ValidationHandler extends StateCallbacks<MSG<JsonObject>, MSG<Object>> {
    protected ValidationHandler(FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG<Object>>>> onEnter, Callable<Promise<Void>> onExit, StateHolder stateHolder) {
        super(enter(stateHolder), onExit);
    }

    private static FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG<Object>>>> enter(StateHolder stateHolder) {
        return msg -> {

            {
                final List<ValidationResult> validationResults = stateHolder.validationPipeline.validate(msg.body);

                if (validationResults == null) {
                    return Promises.from(
                        StateMachine.trigger(EventCn.VALIDATION_FAILED,
                            msg.builder().setBody(validationResults).build()));
                }
            }

            return stateHolder.validationPipelineDeferred.validate(msg.body, msg.store)

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

    public static class StateHolder {
        final ValidationPipeline<JsonObject> validationPipeline;
        final ValidationPipelineDeferred validationPipelineDeferred;

        public StateHolder(ValidationPipeline<JsonObject> validationPipeline, ValidationPipelineDeferred validationPipelineDeferred) {
            this.validationPipeline = validationPipeline == null ? validationPipeline : validators();
            this.validationPipelineDeferred = validationPipelineDeferred == null ? new ValidationPipelineDeferred(ImmutableList.of()) : validationPipelineDeferred;
        }

        private ValidationPipeline<JsonObject> validators() {
            return new ValidationPipeline<>(Collections.emptyList());
        }
    }
}
