package webcomposer.transormations;

import com.google.common.collect.ImmutableList;
import io.crm.transformation.JsonTransformationPipeline;
import io.crm.transformation.Transform;
import io.crm.transformation.impl.json.object.DefaultValueTransformation;
import io.crm.transformation.impl.json.object.RemoveNullsTransformation;
import io.crm.transformation.impl.json.object.StringTrimmer;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by shahadat on 5/8/16.
 */
public class DefaultTransformationPipelineBuilder {
    private List<Transform<JsonObject, JsonObject>> transformList = new ArrayList<>();

    DefaultTransformationPipelineBuilder() {
        transformList = new ArrayList<>();
        transformList.add(new DefaultValueTransformation(new JsonObject()));
        transformList.add(Defaults.ActivityTrackingExcludeTransformation);
        transformList.add(new RemoveNullsTransformation());
        transformList.add(new StringTrimmer(null, null));
    }

    public DefaultTransformationPipelineBuilder configArray(Consumer<List<Transform<JsonObject, JsonObject>>> listConsumer) {
        listConsumer.accept(transformList);
        return this;
    }

    public JsonTransformationPipeline build() {
        return new JsonTransformationPipeline(ImmutableList.copyOf(transformList));
    }
}
