package webcomposer.handlers;

import webcomposer.statehandlers.*;

public class DeleteHandlerComposerBuilder {
    private StartHandler startHandler;
    private TransformationHandler jsonTransformationHandler;
    private ValidationHandler jsonValidationHandler;
    private ValidationErrorHandler validationErrorHandler;
    private DeleteHandler deleteHandler;
    private PublishEventHandler publishEventHandler;
    private EndHandler endHandler;

    public DeleteHandlerComposerBuilder setStartHandler(StartHandler startHandler) {
        this.startHandler = startHandler;
        return this;
    }

    public DeleteHandlerComposerBuilder setJsonTransformationHandler(TransformationHandler jsonTransformationHandler) {
        this.jsonTransformationHandler = jsonTransformationHandler;
        return this;
    }

    public DeleteHandlerComposerBuilder setJsonValidationHandler(ValidationHandler jsonValidationHandler) {
        this.jsonValidationHandler = jsonValidationHandler;
        return this;
    }

    public DeleteHandlerComposerBuilder setValidationErrorHandler(ValidationErrorHandler validationErrorHandler) {
        this.validationErrorHandler = validationErrorHandler;
        return this;
    }

    public DeleteHandlerComposerBuilder setDeleteHandler(DeleteHandler deleteHandler) {
        this.deleteHandler = deleteHandler;
        return this;
    }

    public DeleteHandlerComposerBuilder setPublishEventHandler(PublishEventHandler publishEventHandler) {
        this.publishEventHandler = publishEventHandler;
        return this;
    }

    public DeleteHandlerComposerBuilder setEndHandler(EndHandler endHandler) {
        this.endHandler = endHandler;
        return this;
    }

    public DeleteHandlerComposer createDeleteHandlerComposer() {
        return new DeleteHandlerComposer(startHandler, jsonTransformationHandler, jsonValidationHandler, validationErrorHandler, deleteHandler, publishEventHandler, endHandler);
    }
}