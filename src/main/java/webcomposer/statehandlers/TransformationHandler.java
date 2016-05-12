package webcomposer.statehandlers;

import com.google.common.collect.ImmutableList;
import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateCallbacksBuilder;
import io.crm.statemachine.StateMachine;
import io.crm.statemachine.StateTrigger;
import io.crm.transformation.TransformationPipeline;
import io.crm.transformation.TransformationPipelineDeferred;
import webcomposer.MSG;
import webcomposer.util.EventCn;

/**
 * Created by shahadat on 5/11/16.
 */
public class TransformationHandler {
    final TransformationPipeline transformationPipeline;
    final TransformationPipelineDeferred transformationPipelineDeferred;

    public TransformationHandler(TransformationPipeline transformationPipeline, TransformationPipelineDeferred transformationPipelineDeferred) {
        this.transformationPipeline = transformationPipeline == null ? new TransformationPipeline(ImmutableList.of()) : transformationPipeline;
        this.transformationPipelineDeferred = transformationPipelineDeferred == null ? new TransformationPipelineDeferred(ImmutableList.of()) : transformationPipelineDeferred;
    }

    private FunctionUnchecked<MSG<Object>, Promise<StateTrigger<MSG<Object>>>> enter() {
        return msg -> {
            final Object transform = transformationPipeline.transform(msg.body);
            return transformationPipelineDeferred.transform(transform, msg.store)
                .map(jsonObject -> StateMachine.trigger(EventCn.NEXT, msg.builder().setBody(jsonObject).build()))
                ;
        };
    }

    public StateCallbacks<MSG<Object>, MSG<Object>> toStateCallbacks() {
        return new StateCallbacksBuilder<MSG<Object>, MSG<Object>>()
            .onEnter(enter()).build();
    }
}
