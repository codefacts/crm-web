package webcomposer.handlers;

import io.crm.statemachine.StateMachine;
import webcomposer.statehandlers.*;
import webcomposer.util.EventCn;
import webcomposer.util.StateCn;

import static io.crm.statemachine.StateMachine.*;

/**
 * Created by shahadat on 5/8/16.
 */
public class CreateHandlerComposer {

    final StartHandler startHandler;
    final JsonTransformationHandler jsonTransformationHandler;
    final JsonValidationHandler jsonValidationHandler;
    final ValidationErrorHandler validationErrorHandler;
    final CreateNewHandler createNewHandler;
    final PublishEventHandler publishEventHandler;
    final EndHandler endHandler;

    public CreateHandlerComposer(StartHandler startHandler, JsonTransformationHandler jsonTransformationHandler, JsonValidationHandler jsonValidationHandler, ValidationErrorHandler validationErrorHandler, CreateNewHandler createNewHandler, PublishEventHandler publishEventHandler, EndHandler endHandler) {
        this.startHandler = startHandler;
        this.jsonTransformationHandler = jsonTransformationHandler;
        this.jsonValidationHandler = jsonValidationHandler;
        this.validationErrorHandler = validationErrorHandler;
        this.createNewHandler = createNewHandler;
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
                on(EventCn.VALIDATION_SUCCESS, StateCn.CREATE_NEW))
            .when(StateCn.VALIDATION_ERROR,
                on(EventCn.REPORT_ERROR, StateCn.END))
            .when(StateCn.CREATE_NEW,
                on(EventCn.NEXT, StateCn.PUBLISH_EVENT))
            .when(StateCn.PUBLISH_EVENT, next(StateCn.END))


            .handlers(StateCn.START, startHandler.toStateCallbacks())
            .handlers(StateCn.TRANSFORMATION, jsonTransformationHandler.toStateCallbacks())
            .handlers(StateCn.VALIDATION, jsonValidationHandler.toStateCallbacks())
            .handlers(StateCn.VALIDATION_ERROR, validationErrorHandler.toStateCallbacks())
            .handlers(StateCn.CREATE_NEW, createNewHandler.toStateCallbacks())
            .handlers(StateCn.PUBLISH_EVENT, publishEventHandler.toStateCallbacks())
            .handlers(StateCn.END, endHandler.toStateCallbacks())
            .setInitialState(StateCn.START)
            .build()
            ;
    }
}
