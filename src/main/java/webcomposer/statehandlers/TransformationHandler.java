package webcomposer.statehandlers;

import com.google.common.collect.ImmutableList;
import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateMachine;
import io.crm.statemachine.StateTrigger;
import io.crm.transformation.JsonTransformationPipeline;
import io.crm.transformation.JsonTransformationPipelineDeferred;
import io.vertx.core.json.JsonObject;
import webcomposer.MSG;
import webcomposer.util.EventCn;

import java.util.concurrent.Callable;

/**
 * Created by shahadat on 5/8/16.
 */
public class TransformationHandler extends StateCallbacks<MSG<JsonObject>, MSG<JsonObject>> {
    protected TransformationHandler(FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG<JsonObject>>>> onEnter, Callable<Promise<Void>> onExit, StateHolder stateHolder) {
        super(enter(stateHolder), onExit);
    }

    private static FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG<JsonObject>>>> enter(StateHolder stateHolder) {
        return msg -> {
            final JsonObject transform = stateHolder.transformationPipeline.transform(msg.body);
            return stateHolder.transformationPipelineDeferred.transform(transform, msg.store)
                .map(jsonObject -> StateMachine.trigger(EventCn.NEXT, msg.<JsonObject>builder().setBody(jsonObject).build()))
                ;
        };
    }

    public static final class StateHolder {
        final JsonTransformationPipeline transformationPipeline;
        final JsonTransformationPipelineDeferred transformationPipelineDeferred;

        public StateHolder(JsonTransformationPipeline transformationPipeline, JsonTransformationPipelineDeferred transformationPipelineDeferred) {
            this.transformationPipeline = transformationPipeline == null ? new JsonTransformationPipeline(ImmutableList.of()) : transformationPipeline;
            this.transformationPipelineDeferred = transformationPipelineDeferred == null ? new JsonTransformationPipelineDeferred(ImmutableList.of()) : transformationPipelineDeferred;
        }
    }
}
