package webcomposer.statehandlers;

import com.google.common.collect.ImmutableList;
import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateCallbacksBuilder;
import io.crm.statemachine.StateMachine;
import io.crm.statemachine.StateTrigger;
import io.crm.transformation.JsonTransformationPipeline;
import io.crm.transformation.JsonTransformationPipelineDeferred;
import io.vertx.core.json.JsonObject;
import webcomposer.MSG;
import webcomposer.util.EventCn;

/**
 * Created by shahadat on 5/8/16.
 */
public class TransformationHandler {
    final JsonTransformationPipeline transformationPipeline;
    final JsonTransformationPipelineDeferred transformationPipelineDeferred;

    public TransformationHandler(JsonTransformationPipeline transformationPipeline, JsonTransformationPipelineDeferred transformationPipelineDeferred) {
        this.transformationPipeline = transformationPipeline == null ? new JsonTransformationPipeline(ImmutableList.of()) : transformationPipeline;
        this.transformationPipelineDeferred = transformationPipelineDeferred == null ? new JsonTransformationPipelineDeferred(ImmutableList.of()) : transformationPipelineDeferred;
    }

    private FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG<JsonObject>>>> enter() {
        return msg -> {
            final JsonObject transform = transformationPipeline.transform(msg.body);
            return transformationPipelineDeferred.transform(transform, msg.store)
                .map(jsonObject -> StateMachine.trigger(EventCn.NEXT, msg.<JsonObject>builder().setBody(jsonObject).build()))
                ;
        };
    }

    public StateCallbacks<MSG<JsonObject>, MSG<JsonObject>> toStateCallbacks() {
        return new StateCallbacksBuilder<MSG<JsonObject>, MSG<JsonObject>>()
            .onEnter(enter()).build();
    }
}
