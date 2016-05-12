package webcomposer.handlers;

import webcomposer.statehandlers.*;

public class CreateHandlerComposerBuilder {
    private StartHandler startHandler;
    private JsonTransformationHandler jsonTransformationHandler;
    private JsonValidationHandler jsonValidationHandler;
    private ValidationErrorHandler validationErrorHandler;
    private CreateNewHandler createNewHandler;
    private PublishEventHandler publishEventHandler;
    private EndHandler endHandler;

    public CreateHandlerComposerBuilder setStartHandler(StartHandler startHandler) {
        this.startHandler = startHandler;
        return this;
    }

    public CreateHandlerComposerBuilder setJsonTransformationHandler(JsonTransformationHandler jsonTransformationHandler) {
        this.jsonTransformationHandler = jsonTransformationHandler;
        return this;
    }

    public CreateHandlerComposerBuilder setJsonValidationHandler(JsonValidationHandler jsonValidationHandler) {
        this.jsonValidationHandler = jsonValidationHandler;
        return this;
    }

    public CreateHandlerComposerBuilder setValidationErrorHandler(ValidationErrorHandler validationErrorHandler) {
        this.validationErrorHandler = validationErrorHandler;
        return this;
    }

    public CreateHandlerComposerBuilder setCreateNewHandler(CreateNewHandler createNewHandler) {
        this.createNewHandler = createNewHandler;
        return this;
    }

    public CreateHandlerComposerBuilder setPublishEventHandler(PublishEventHandler publishEventHandler) {
        this.publishEventHandler = publishEventHandler;
        return this;
    }

    public CreateHandlerComposerBuilder setEndHandler(EndHandler endHandler) {
        this.endHandler = endHandler;
        return this;
    }

    public CreateHandlerComposer createCreateHandlerComposer() {
        return new CreateHandlerComposer(startHandler, jsonTransformationHandler, jsonValidationHandler, validationErrorHandler, createNewHandler, publishEventHandler, endHandler);
    }
}