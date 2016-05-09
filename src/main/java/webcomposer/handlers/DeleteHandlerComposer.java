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

    final StartHandlerJsonObject startHandlerJsonObject;
    final TransformationHandler transformationHandler;
    final ValidationHandler validationHandler;
    final ValidationErrorHandler validationErrorHandler;
    final DeleteHandler deleteHandler;
    final PublishEventHandler publishEventHandler;
    final EndHandler endHandler;

    public DeleteHandlerComposer(StartHandlerJsonObject startHandlerJsonObject, TransformationHandler transformationHandler, ValidationHandler validationHandler, ValidationErrorHandler validationErrorHandler, DeleteHandler deleteHandler, PublishEventHandler publishEventHandler, EndHandler endHandler) {
        this.startHandlerJsonObject = startHandlerJsonObject;
        this.transformationHandler = transformationHandler;
        this.validationHandler = validationHandler;
        this.validationErrorHandler = validationErrorHandler;
        this.deleteHandler = deleteHandler;
        this.publishEventHandler = publishEventHandler;
        this.endHandler = endHandler;
    }

    private final StateMachine build() {

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
                on(EventCn.SUCCESS, StateCn.PUBLISH_EVENT))
            .when(StateCn.PUBLISH_EVENT, next(StateCn.END))

            .handlers(StateCn.START, startHandlerJsonObject.toStateCallbacks())
            .handlers(StateCn.TRANSFORMATION, transformationHandler.toStateCallbacks())
            .handlers(StateCn.VALIDATION, validationHandler.toStateCallbacks())
            .handlers(StateCn.VALIDATION_ERROR, validationErrorHandler.toStateCallbacks())
            .handlers(StateCn.DELETE, deleteHandler.toStateCallbacks())
            .handlers(StateCn.PUBLISH_EVENT, publishEventHandler.toStateCallbacks())
            .handlers(StateCn.END, endHandler.toStateCallbacks())
            .build()
            ;
    }
}
