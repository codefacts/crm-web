package webcomposer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.CallbacksBuilder;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateMachineBuilder;
import io.crm.transformation.JsonTransformationPipeline;
import io.crm.transformation.JsonTransformationPipelineDeferred;
import io.crm.transformation.Transform;
import io.crm.transformation.TransformDeferred;
import io.crm.transformation.impl.json.object.ConverterTransformation;
import io.crm.transformation.impl.json.object.DefaultValueTransformation;
import io.crm.transformation.impl.json.object.RemoveNullsTransformation;
import io.crm.util.Context;
import io.crm.validator.ValidationPipeline;
import io.crm.validator.ValidationPipelineDeferred;
import io.crm.validator.Validator;
import io.crm.validator.ValidatorDeferred;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message<JsonObject>;
import io.vertx.core.json.JsonObject;
import webcomposer.chain.cleanups.DbCleaner;
import webcomposer.chain.initializers.DbConInitializer;
import webcomposer.transormations.Defaults;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.crm.statemachine.States.on;

/**
 * Created by shahadat on 5/3/16.
 */
public class CreateHandlerComposer {
    private final DomainInfo domainInfo;
    private final Vertx vertx;

    private final ValidationPipeline<JsonObject> validationPipeline;
    private final ValidationPipelineDeferred validationPipelineDeferred;

    private final JsonTransformationPipeline transformationPipeline;
    private final JsonTransformationPipelineDeferred transformationPipelineDeferred;
    private final StateMachineBuilder stateMachineBuilder;

    private final List<Function<Message<JsonObject>, Promise<Map<String, Object>>>> initializeList;
    private final List<BiFunction<Context, Message<JsonObject>, Promise<Map<String, Object>>>> executeList;
    private final List<BiFunction<Context, Message<JsonObject>, Promise<Void>>> cleanupList;

    private final List<String> fields;
    private final ImmutableMap<String, Function<Object, Object>> converters;
    private final JsonObject dbConfig;

    public CreateHandlerComposer(DomainInfo domainInfo, Vertx vertx,
                                 List<String> fields, ImmutableMap<String, Function<Object, Object>> converters,
                                 JsonObject dbConfig) {
        this.domainInfo = domainInfo;
        this.vertx = vertx;
        this.fields = fields;
        this.converters = converters;
        this.dbConfig = dbConfig;


        validationPipeline = validators();

        validationPipelineDeferred = validatorsDeferred();

        transformationPipeline = transformations();

        transformationPipelineDeferred = transformationsDeferred();

        initializeList = initializeList();

        executeList = executeList();

        cleanupList = cleanupList();

        stateMachineBuilder = stateMachineBuilder();
    }

    //Pipelines
    public CreateHandlerComposer confValidations(Consumer<ValidationPipeline<JsonObject>> listConsumer) {
        listConsumer.accept(validationPipeline);
        return this;
    }

    public CreateHandlerComposer confValidationsDeferred(Consumer<ValidationPipelineDeferred> listConsumer) {
        listConsumer.accept(validationPipelineDeferred);
        return this;
    }

    public CreateHandlerComposer confTransformations(Consumer<JsonTransformationPipeline> listConsumer) {
        listConsumer.accept(transformationPipeline);
        return this;
    }

    public CreateHandlerComposer confTransformationsDeferred(Consumer<JsonTransformationPipelineDeferred> listConsumer) {
        listConsumer.accept(transformationPipelineDeferred);
        return this;
    }

    //State Machine
    public CreateHandlerComposer confCleanupList(Consumer<List<BiFunction<Context, Message<JsonObject>, Promise<Void>>>> listConsumer) {
        listConsumer.accept(cleanupList);
        return this;
    }

    public CreateHandlerComposer confExecuteList(Consumer<List<BiFunction<Context, Message<JsonObject>, Promise<Map<String, Object>>>>> listConsumer) {
        listConsumer.accept(executeList);
        return this;
    }

    public CreateHandlerComposer confInitializeList(Consumer<List<Function<Message<JsonObject>, Promise<Map<String, Object>>>>> listConsumer) {
        listConsumer.accept(initializeList);
        return this;
    }

    //Functions
    private List<BiFunction<Context, Message<JsonObject>, Promise<Map<String, Object>>>> executeList() {
        List<BiFunction<Context, Message<JsonObject>, Promise<Map<String, Object>>>> list = new ArrayList<>();
        return list;
    }

    private List<Function<Message<JsonObject>, Promise<Map<String, Object>>>> initializeList() {
        List<Function<Message<JsonObject>, Promise<Map<String, Object>>>> initializers = new ArrayList<>();

        initializers.add(
            new DbConInitializer<>(vertx, dbConfig)
        );

        return initializers;
    }

    private List<BiFunction<Context, Message<JsonObject>, Promise<Void>>> cleanupList() {
        List<BiFunction<Context, Message<JsonObject>, Promise<Void>>> cleanups = new ArrayList<>();

        cleanups.add(
            new DbCleaner()
        );

        return cleanups;
    }


    //State Machine
    private StateMachineBuilder stateMachineBuilder() {
        return new StateMachineBuilder(Cnst.START_STATE)

            .from(Cnst.START_STATE,

                on(Cnst.VALIDATION_ERROR_EVENT).to(Cnst.VALIDATION_ERROR_STATE),

                on(Cnst.VALIDATION_SUCCESS_EVENT).to(Cnst.CREATE_NEW_STATE))

            .from(Cnst.CREATE_NEW_STATE, on(Cnst.CREATE_SUCCESS_EVENT).to(Cnst.END_STATE))

            .from(Cnst.VALIDATION_ERROR_STATE, on(Cnst.NEXT_TO_EVENT).to(Cnst.END_STATE))

            .callbacks(Cnst.CREATE_NEW_STATE,
                new CallbacksBuilder<Message<JsonObject>>()
                    .setInitialize(this::initializer)
                    .setCleanup(this::cleanup)
                    .setExecute((context1, message1) -> {



                    })
                    .createState())
            .callbacks(Cnst.VALIDATION_ERROR_STATE,
                new CallbacksBuilder<>()
                    .setExecute((context, o) -> {

                    })
                    .createState())
            ;
    }

    private Promise<Void> cleanup(Context context, Message<JsonObject> message) {

        ImmutableList.Builder<Promise<Void>> promiseList = ImmutableList.<Promise<Void>>builder();

        cleanupList.forEach(contextMessagePromiseBiFunction -> promiseList.add(contextMessagePromiseBiFunction.apply(context, message)));

        return Promises.when(promiseList.build()).map(voids -> null);
    }

    private Promise<Map<String, Object>> initializer(Message<JsonObject> message) {
        final ImmutableList.Builder<Promise<Map<String, Object>>> promiseBuilder =
            ImmutableList.<Promise<Map<String, Object>>>builder();
        initializeList.forEach(messagePromiseFunction -> promiseBuilder.add(messagePromiseFunction.apply(message)));
        return Promises.when(promiseBuilder.build())
            .map(maps -> {
                final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

                maps.forEach(builder::putAll);

                return builder.build();
            });
    }

    private JsonTransformationPipelineDeferred transformationsDeferred() {
        List<TransformDeferred<JsonObject, JsonObject>> list = new ArrayList<>();
        return new JsonTransformationPipelineDeferred(list);
    }

    private JsonTransformationPipeline transformations() {
        final ArrayList<Transform<JsonObject, JsonObject>> list = new ArrayList<>();

        list.add(new RemoveNullsTransformation(null, null));
        list.add(new DefaultValueTransformation(new JsonObject()));
        list.add(Defaults.ActivityTrackingExcludeTransformation);

        list.add(new ConverterTransformation(converters));

        return new JsonTransformationPipeline(list);
    }

    private ValidationPipelineDeferred validatorsDeferred() {
        List<ValidatorDeferred<JsonObject>> list = new ArrayList<>();
        return new ValidationPipelineDeferred(list);
    }

    private ValidationPipeline<JsonObject> validators() {
        List<Validator<JsonObject>> list = new ArrayList<>();
        return new ValidationPipeline<>(list);
    }

    //Builder
    public CreateHandlerComposer confStateMachineBuilder(Consumer<StateMachineBuilder> builderConsumer) {
        builderConsumer.accept(stateMachineBuilder);
        return this;
    }
}
