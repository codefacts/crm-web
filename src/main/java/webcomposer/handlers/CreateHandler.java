package webcomposer.handlers;

import io.crm.statemachine.StateMachine;
import webcomposer.statehandlers.CreateNewHandler;
import webcomposer.statehandlers.EndHandler;
import webcomposer.statehandlers.JsonObjectStartHandler;
import webcomposer.statehandlers.ValidationErrorHandler;
import webcomposer.util.EventCn;
import webcomposer.util.StateCn;

import static io.crm.statemachine.StateMachine.on;

/**
 * Created by shahadat on 5/8/16.
 */
public class CreateHandler {

    private final StateMachine stateMachine;

    private final StateMachine build() {

        return StateMachine.builder()
            .when(StateCn.START,
                on(EventCn.VALIDATION_FAILED, StateCn.VALIDATION_ERROR),
                on(EventCn.VALIDATION_SUCCESS, StateCn.CREATE_NEW))
            .when(StateCn.VALIDATION_ERROR,
                on(EventCn.REPLTY_ERROR, StateCn.END))
            .when(StateCn.CREATE_NEW,
                on(EventCn.SUCCESS, StateCn.END))

            .handlers(StateCn.START, new JsonObjectStartHandler(pipeline, pipelineDeferred, validationPipeline, validationPipelineDeferred))
            .handlers(StateCn.END, new EndHandler(storeHolder))
            .handlers(StateCn.CREATE_NEW, new CreateNewHandler())
            .handlers(StateCn.VALIDATION_ERROR, new ValidationErrorHandler())
            .build()
            ;
    }
}
