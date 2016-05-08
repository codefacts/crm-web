package webcomposer.statehandlers;

import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateMachine;
import io.crm.statemachine.StateTrigger;
import io.crm.transformation.JsonTransformationPipeline;
import io.crm.transformation.JsonTransformationPipelineDeferred;
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
public class JsonObjectStartHandler extends StateCallbacks<MSG<JsonObject>, MSG> {

    public JsonObjectStartHandler(StateHolder stateHolder) {
        super(enter(stateHolder), null);
    }

    private static FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG>>> enter(StateHolder stateHolder) {

        return msg -> {
            final JsonObject transform = stateHolder.pipeline.transform(msg.body);
            final List<ValidationResult> validationResults = stateHolder.validationPipeline.validate(transform);
            if (validationResults != null) {
                return Promises.from(StateMachine.trigger(EventCn.VALIDATION_FAILED, msg.builder().setBody(validationResults).build()));
            }

            return Promises.from(StateMachine.trigger(EventCn.VALIDATION_SUCCESS, msg.builder().setBody(transform).build()));
        };
    }

    public static class StateHolder {
        private final JsonTransformationPipeline pipeline;
        private final ValidationPipeline<JsonObject> validationPipeline;

        public StateHolder(JsonTransformationPipeline pipeline, JsonTransformationPipelineDeferred pipelineDeferred, ValidationPipeline<JsonObject> validationPipeline, ValidationPipelineDeferred validationPipelineDeferred) {
            this.pipeline = pipeline == null ? new JsonTransformationPipeline(Collections.emptyList()) : pipeline;
            this.validationPipeline = validationPipeline == null ? new ValidationPipeline<>(Collections.emptyList()) : validationPipeline;
        }
    }
}
