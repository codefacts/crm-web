package webcomposer.statehandlers;

import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateCallbacksBuilder;
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
public class StartHandlerJsonObject {

    private final JsonTransformationPipeline pipeline;

    public StartHandlerJsonObject(JsonTransformationPipeline pipeline, JsonTransformationPipelineDeferred pipelineDeferred, ValidationPipeline<JsonObject> validationPipeline, ValidationPipelineDeferred validationPipelineDeferred) {
        this.pipeline = pipeline == null ? new JsonTransformationPipeline(Collections.emptyList()) : pipeline;
    }

    private FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG>>> enter() {

        return msg -> {

            final JsonObject transform = pipeline.transform(msg.body);

            return Promises.from(StateMachine.trigger(EventCn.NEXT, msg.builder().setBody(transform).build()));
        };
    }

    public StateCallbacks<MSG<JsonObject>, MSG> toStateCallbacks() {
        return new StateCallbacksBuilder<MSG<JsonObject>, MSG>()
            .onEnter(enter()).build();
    }
}
