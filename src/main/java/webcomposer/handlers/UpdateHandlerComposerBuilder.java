package webcomposer.handlers;

import webcomposer.statehandlers.*;

public class UpdateHandlerComposerBuilder {
    private StartHandler startHandler;
    private JsonTransformationHandler jsonTransformationHandler;
    private JsonValidationHandler jsonValidationHandler;
    private ValidationErrorHandler validationErrorHandler;
    private UpdateHandler updateHandler;
    private PublishEventHandler publishEventHandler;
    private EndHandler endHandler;

    public UpdateHandlerComposerBuilder setStartHandler(StartHandler startHandler) {
        this.startHandler = startHandler;
        return this;
    }

    public UpdateHandlerComposerBuilder setJsonTransformationHandler(JsonTransformationHandler jsonTransformationHandler) {
        this.jsonTransformationHandler = jsonTransformationHandler;
        return this;
    }

    public UpdateHandlerComposerBuilder setJsonValidationHandler(JsonValidationHandler jsonValidationHandler) {
        this.jsonValidationHandler = jsonValidationHandler;
        return this;
    }

    public UpdateHandlerComposerBuilder setValidationErrorHandler(ValidationErrorHandler validationErrorHandler) {
        this.validationErrorHandler = validationErrorHandler;
        return this;
    }

    public UpdateHandlerComposerBuilder setUpdateHandler(UpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
        return this;
    }

    public UpdateHandlerComposerBuilder setPublishEventHandler(PublishEventHandler publishEventHandler) {
        this.publishEventHandler = publishEventHandler;
        return this;
    }

    public UpdateHandlerComposerBuilder setEndHandler(EndHandler endHandler) {
        this.endHandler = endHandler;
        return this;
    }

    public UpdateHandlerComposer createUpdateHandlerComposer() {
        return new UpdateHandlerComposer(startHandler, jsonTransformationHandler, jsonValidationHandler, validationErrorHandler, updateHandler, publishEventHandler, endHandler);
    }
}