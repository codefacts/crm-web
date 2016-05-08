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
import io.vertx.core.json.JsonObject;
import webcomposer.MSG;
import webcomposer.util.EventCn;

import java.util.Collections;

/**
 * Created by shahadat on 5/8/16.
 */
public class StartHandlerJsonObject extends StateCallbacks<MSG<JsonObject>, MSG> {

    public StartHandlerJsonObject(StateHolder stateHolder) {
        super(enter(stateHolder), null);
    }

    private static FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG>>> enter(StateHolder stateHolder) {

        return msg -> {

            final JsonObject transform = stateHolder.pipeline.transform(msg.body);

            return Promises.from(StateMachine.trigger(EventCn.NEXT, msg.builder().setBody(transform).build()));
        };
    }

    public static class StateHolder {
        private final JsonTransformationPipeline pipeline;

        public StateHolder(JsonTransformationPipeline pipeline, JsonTransformationPipelineDeferred pipelineDeferred, ValidationPipeline<JsonObject> validationPipeline, ValidationPipelineDeferred validationPipelineDeferred) {
            this.pipeline = pipeline == null ? new JsonTransformationPipeline(Collections.emptyList()) : pipeline;
        }
    }
}
