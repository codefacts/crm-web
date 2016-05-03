package webcomposer;

import io.crm.transformation.JsonTransformationPipeline;
import io.crm.transformation.JsonTransformationPipelineDeferred;
import io.crm.validator.ValidationPipeline;
import io.crm.validator.ValidationPipelineDeferred;
import io.vertx.core.Vertx;
import io.vertx.ext.jdbc.JDBCClient;

/**
 * Created by shahadat on 5/3/16.
 */
public class CreateHandlerComposer {
    private final DomainInfo domainInfo;
    private final Vertx vertx;
    private final JDBCClient jdbcClient;

    private final ValidationPipeline validationPipeline;
    private final ValidationPipelineDeferred validationPipelineDeferred;

    private final JsonTransformationPipeline jsonTransformationPipeline;
    private final JsonTransformationPipelineDeferred jsonTransformationPipelineDeferred;

    public CreateHandlerComposer(DomainInfo domainInfo, Vertx vertx, JDBCClient jdbcClient, ValidationPipeline validationPipeline, ValidationPipelineDeferred validationPipelineDeferred, JsonTransformationPipeline jsonTransformationPipeline, JsonTransformationPipelineDeferred jsonTransformationPipelineDeferred) {
        this.domainInfo = domainInfo;
        this.vertx = vertx;
        this.jdbcClient = jdbcClient;
        this.validationPipeline = validationPipeline;
        this.validationPipelineDeferred = validationPipelineDeferred;
        this.jsonTransformationPipeline = jsonTransformationPipeline;
        this.jsonTransformationPipelineDeferred = jsonTransformationPipelineDeferred;
    }
}
