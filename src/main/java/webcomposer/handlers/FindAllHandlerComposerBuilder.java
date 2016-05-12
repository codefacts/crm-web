package webcomposer.handlers;

import webcomposer.statehandlers.*;

public class FindAllHandlerComposerBuilder {
    private StartHandler startHandler;
    private JsonTransformationHandler jsonTransformationHandler;
    private JsonValidationHandler jsonValidationHandler;
    private ValidationErrorHandler validationErrorHandler;
    private FIndAllHandler findAllHandler;
    private EndHandler endHandler;

    public FindAllHandlerComposerBuilder setStartHandler(StartHandler startHandler) {
        this.startHandler = startHandler;
        return this;
    }

    public FindAllHandlerComposerBuilder setJsonTransformationHandler(JsonTransformationHandler jsonTransformationHandler) {
        this.jsonTransformationHandler = jsonTransformationHandler;
        return this;
    }

    public FindAllHandlerComposerBuilder setJsonValidationHandler(JsonValidationHandler jsonValidationHandler) {
        this.jsonValidationHandler = jsonValidationHandler;
        return this;
    }

    public FindAllHandlerComposerBuilder setValidationErrorHandler(ValidationErrorHandler validationErrorHandler) {
        this.validationErrorHandler = validationErrorHandler;
        return this;
    }

    public FindAllHandlerComposerBuilder setFindAllHandler(FIndAllHandler findAllHandler) {
        this.findAllHandler = findAllHandler;
        return this;
    }

    public FindAllHandlerComposerBuilder setEndHandler(EndHandler endHandler) {
        this.endHandler = endHandler;
        return this;
    }

    public FindAllHandlerComposer createFindAllHandlerComposer() {
        return new FindAllHandlerComposer(startHandler, jsonTransformationHandler, jsonValidationHandler, validationErrorHandler, findAllHandler, endHandler);
    }
}