package webcomposer;

import io.crm.ErrorCodes;
import io.crm.MessageBundle;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.CallbacksBuilder;
import io.crm.statemachine.StateContext;
import io.crm.statemachine.StateMachineBuilder;
import io.crm.util.Context;
import io.crm.validator.ValidationResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static io.crm.statemachine.States.on;

/**
 * Created by shahadat on 5/3/16.
 */
public class ChainComposer<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChainComposer.class);

    private final StateLifeCycle<T, Object> startStateLifeCycle;

    private final StateMachineBuilder stateMachineBuilder;

    private final Vertx vertx;
    private final JDBCClient jdbcClient;
    private final MessageBundle messageBundle;
    private final Store globalStore;

    public ChainComposer(Vertx vertx, JDBCClient jdbcClient, MessageBundle messageBundle, Store globalStore) {
        this.vertx = vertx;
        this.jdbcClient = jdbcClient;
        this.messageBundle = messageBundle;
        this.globalStore = globalStore;

        this.startStateLifeCycle = startStateLifeCycle();

        this.stateMachineBuilder = stateMachineBuilder();
    }

    //State Machine
    private StateMachineBuilder stateMachineBuilder() {

        return new StateMachineBuilder(Cnst.START_STATE)

            .from(Cnst.START_STATE,

                on(Cnst.VALIDATION_ERROR_EVENT).to(Cnst.VALIDATION_ERROR_STATE))

            .from(Cnst.VALIDATION_ERROR_STATE, on(Cnst.GOTO_END_EVENT).to(Cnst.END_STATE))


            .callbacks(Cnst.START_STATE, new CallbacksBuilder<T, Object>()
                .setInitialize(startStateLifeCycle::initialize)
                .setExecute(startStateLifeCycle::executor)
                .setCleanup(startStateLifeCycle::cleanup)
                .createState())

            .callbacks(Cnst.END_STATE, new CallbacksBuilder<MSG, Void>()
                .setExecute(this::endStateExecute)
                .createState())

            .callbacks(Cnst.VALIDATION_ERROR_STATE,
                new CallbacksBuilder<List<ValidationResult>, MSG<JsonObject>>()
                    .setExecute(this::validationError)
                    .createState())
            ;
    }

    private Promise<StateContext<Void>> endStateExecute(Context context, MSG msg) {

        final Message message = context.getAs(Cnst.MESSAGE);

        if (msg.deliveryOptions != null) {

            message.reply(msg.body, msg.deliveryOptions);

        } else {

            message.reply(msg.body);
        }

        return null;
    }

    private Promise<StateContext<MSG<JsonObject>>> validationError(Context context, List<ValidationResult> list) {
        return Promises.from(
            new StateContext<>(null, Cnst.GOTO_END_EVENT,
                new MSG<>(
                    new JsonObject()
                        .put(Resp.CODE, ErrorCodes.VALIDATION_ERROR.code())
                        .put(Resp.MESSAGE_CODE, ErrorCodes.VALIDATION_ERROR.messageCode())
                        .put(Resp.MESSAGE, messageBundle.translate(ErrorCodes.VALIDATION_ERROR.messageCode(),
                            new JsonObject()
                                .put(Cnst.VALIDATION_RESULTS,
                                    list.stream()
                                        .map(ValidationResult::toJson)
                                        .collect(Collectors.toList())))))));
    }

    //Private
    private StateLifeCycle<T, Object> startStateLifeCycle() {
        return new StateLifeCycle<>();
    }

    //Builder
    public ChainComposer<T> confStateLifeCycle(Consumer<StateLifeCycle<T, Object>> cycleConsumer) {
        cycleConsumer.accept(startStateLifeCycle);
        return this;
    }

    //Builder
    public ChainComposer<T> confStateMachineBuilder(Consumer<StateMachineBuilder> builderConsumer) {
        builderConsumer.accept(stateMachineBuilder);
        return this;
    }
}

