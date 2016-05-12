package webcomposer.handlers;

import io.crm.statemachine.StateMachine;
import webcomposer.statehandlers.*;
import webcomposer.util.EventCn;
import webcomposer.util.StateCn;

import static io.crm.statemachine.StateMachine.builder;
import static io.crm.statemachine.StateMachine.next;
import static io.crm.statemachine.StateMachine.on;

/**
 * Created by shahadat on 5/9/16.
 */
public class DeleteHandlerComposer {

    final StartHandler startHandler;
    final TransformationHandler jsonTransformationHandler;
    final ValidationHandler jsonValidationHandler;
    final ValidationErrorHandler validationErrorHandler;
    final DeleteHandler deleteHandler;
    final PublishEventHandler publishEventHandler;
    final EndHandler endHandler;

    public DeleteHandlerComposer(StartHandler startHandler, TransformationHandler jsonTransformationHandler, ValidationHandler jsonValidationHandler, ValidationErrorHandler validationErrorHandler, DeleteHandler deleteHandler, PublishEventHandler publishEventHandler, EndHandler endHandler) {
        this.startHandler = startHandler;
        this.jsonTransformationHandler = jsonTransformationHandler;
        this.jsonValidationHandler = jsonValidationHandler;
        this.validationErrorHandler = validationErrorHandler;
        this.deleteHandler = deleteHandler;
        this.publishEventHandler = publishEventHandler;
        this.endHandler = endHandler;
    }

    public final StateMachine build() {

        return builder()
            .when(StateCn.START, next(StateCn.TRANSFORMATION))
            .when(StateCn.TRANSFORMATION,
                on(EventCn.NEXT, StateCn.VALIDATION))
            .when(StateCn.VALIDATION,
                on(EventCn.VALIDATION_FAILED, StateCn.VALIDATION_ERROR),
                on(EventCn.VALIDATION_SUCCESS, StateCn.DELETE))
            .when(StateCn.VALIDATION_ERROR,
                on(EventCn.REPORT_ERROR, StateCn.END))
            .when(StateCn.DELETE,
                on(EventCn.NEXT, StateCn.PUBLISH_EVENT))
            .when(StateCn.PUBLISH_EVENT, next(StateCn.END))

            .handlers(StateCn.START, startHandler.toStateCallbacks())
            .handlers(StateCn.TRANSFORMATION, jsonTransformationHandler.toStateCallbacks())
            .handlers(StateCn.VALIDATION, jsonValidationHandler.toStateCallbacks())
            .handlers(StateCn.VALIDATION_ERROR, validationErrorHandler.toStateCallbacks())
            .handlers(StateCn.DELETE, deleteHandler.toStateCallbacks())
            .handlers(StateCn.PUBLISH_EVENT, publishEventHandler.toStateCallbacks())
            .handlers(StateCn.END, endHandler.toStateCallbacks())
            .setInitialState(StateCn.START)
            .build()
            ;
    }
}
