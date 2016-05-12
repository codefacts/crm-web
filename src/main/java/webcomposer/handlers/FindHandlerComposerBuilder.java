package webcomposer.handlers;

import webcomposer.statehandlers.*;

public class FindHandlerComposerBuilder {
    private StartHandler startHandler;
    private TransformationHandler jsonTransformationHandler;
    private ValidationHandler jsonValidationHandler;
    private ValidationErrorHandler validationErrorHandler;
    private FindHandler findHandler;
    private EndHandler endHandler;

    public FindHandlerComposerBuilder setStartHandler(StartHandler startHandler) {
        this.startHandler = startHandler;
        return this;
    }

    public FindHandlerComposerBuilder setJsonTransformationHandler(TransformationHandler jsonTransformationHandler) {
        this.jsonTransformationHandler = jsonTransformationHandler;
        return this;
    }

    public FindHandlerComposerBuilder setJsonValidationHandler(ValidationHandler jsonValidationHandler) {
        this.jsonValidationHandler = jsonValidationHandler;
        return this;
    }

    public FindHandlerComposerBuilder setValidationErrorHandler(ValidationErrorHandler validationErrorHandler) {
        this.validationErrorHandler = validationErrorHandler;
        return this;
    }

    public FindHandlerComposerBuilder setFindHandler(FindHandler findHandler) {
        this.findHandler = findHandler;
        return this;
    }

    public FindHandlerComposerBuilder setEndHandler(EndHandler endHandler) {
        this.endHandler = endHandler;
        return this;
    }

    public FindHandlerComposer createFindHandlerComposer() {
        return new FindHandlerComposer(startHandler, jsonTransformationHandler, jsonValidationHandler, validationErrorHandler, findHandler, endHandler);
    }
}