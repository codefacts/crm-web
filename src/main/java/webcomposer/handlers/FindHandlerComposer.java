package webcomposer.handlers;

import io.crm.statemachine.StateMachine;
import webcomposer.statehandlers.*;
import webcomposer.util.EventCn;
import webcomposer.util.StateCn;

import static io.crm.statemachine.StateMachine.builder;
import static io.crm.statemachine.StateMachine.next;
import static io.crm.statemachine.StateMachine.on;

/**
 * Created by shahadat on 5/3/16.
 */
public class FindHandlerComposer {
    final StartHandlerJsonObject startHandlerJsonObject;
    final TransformationHandler transformationHandler;
    final ValidationHandler validationHandler;
    final ValidationErrorHandler validationErrorHandler;
    final FindHandler findHandler;
    final EndHandler endHandler;

    public FindHandlerComposer(StartHandlerJsonObject startHandlerJsonObject, TransformationHandler transformationHandler, ValidationHandler validationHandler, ValidationErrorHandler validationErrorHandler, FindHandler findHandler, EndHandler endHandler) {
        this.startHandlerJsonObject = startHandlerJsonObject;
        this.transformationHandler = transformationHandler;
        this.validationHandler = validationHandler;
        this.validationErrorHandler = validationErrorHandler;
        this.findHandler = findHandler;
        this.endHandler = endHandler;
    }

    private final StateMachine build() {

        return builder()
            .when(StateCn.START, next(StateCn.TRANSFORMATION))
            .when(StateCn.TRANSFORMATION,
                on(EventCn.NEXT, StateCn.VALIDATION))
            .when(StateCn.VALIDATION,
                on(EventCn.VALIDATION_FAILED, StateCn.VALIDATION_ERROR),
                on(EventCn.VALIDATION_SUCCESS, StateCn.FIND))
            .when(StateCn.VALIDATION_ERROR,
                on(EventCn.REPORT_ERROR, StateCn.END))
            .when(StateCn.FIND,
                on(EventCn.SUCCESS, StateCn.END))


            .handlers(StateCn.START, startHandlerJsonObject.toStateCallbacks())
            .handlers(StateCn.TRANSFORMATION, transformationHandler.toStateCallbacks())
            .handlers(StateCn.VALIDATION, validationHandler.toStateCallbacks())
            .handlers(StateCn.VALIDATION_ERROR, validationErrorHandler.toStateCallbacks())
            .handlers(StateCn.FIND_ALL, findHandler.toStateCallbacks())
            .handlers(StateCn.END, endHandler.toStateCallbacks())
            .build()
            ;
    }
}
