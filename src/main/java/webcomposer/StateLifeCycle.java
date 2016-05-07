package webcomposer;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateContext;
import io.crm.util.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by shahadat on 5/4/16.
 */
public class StateLifeCycle<T, R> {

    public static final Logger LOGGER = LoggerFactory.getLogger(StateLifeCycle.class);

    private final List<Function<T, Promise<Map<String, Object>>>> initializeList;
    private final List<BiFunction<Context, T, Promise<Map<String, Object>>>> executeList;
    private final List<BiFunction<Context, T, Promise<Void>>> cleanupList;

    private Function<Map<String, Object>, StateContext<R>> triggerNext;

    public StateLifeCycle() {

        initializeList = initializeList();

        executeList = executeList();

        cleanupList = cleanupList();
    }

    public Function<Map<String, Object>, StateContext<R>> getTriggerNext() {
        return triggerNext;
    }

    public StateLifeCycle<T, R> setTriggerNext(Function<Map<String, Object>, StateContext<R>> triggerNext) {
        this.triggerNext = triggerNext;
        return this;
    }

    public StateLifeCycle(
        List<Function<T, Promise<Map<String, Object>>>> initializeLst,
        List<BiFunction<Context, T, Promise<Map<String, Object>>>> executeLst,
        List<BiFunction<Context, T, Promise<Void>>> cleanupLst) {

        this.initializeList = initializeLst;

        this.executeList = executeLst;

        this.cleanupList = cleanupLst;
    }

    //State Machine
    public StateLifeCycle<T, R> confCleanupList(
        Consumer<List<BiFunction<Context, T, Promise<Void>>>> listConsumer) {
        listConsumer.accept(cleanupList);
        return this;
    }

    public StateLifeCycle<T, R> confExecuteList(
        Consumer<List<BiFunction<Context, T, Promise<Map<String, Object>>>>> listConsumer) {
        listConsumer.accept(executeList);
        return this;
    }

    public StateLifeCycle<T, R> confInitializeList(
        Consumer<List<Function<T, Promise<Map<String, Object>>>>> listConsumer) {
        listConsumer.accept(initializeList);
        return this;
    }

    public Promise<StateContext<R>> executor(Context context, T message) {

        final ImmutableList.Builder<Promise<Map<String, Object>>> builder = ImmutableList.<Promise<Map<String, Object>>>builder();

        executeList.forEach(function -> builder.add(function.apply(context, message)));

        return Promises.when(builder.build())
            .map(maps -> {
                final ImmutableMap.Builder<String, Object> builder1 = ImmutableMap.<String, Object>builder();

                maps.forEach(builder1::putAll);

                final ImmutableMap<String, Object> immutableMap = builder1.build();

                return triggerNext.apply(immutableMap);

            });
    }

    public Promise<Void> cleanup(Context context, T message) {

        ImmutableList.Builder<Promise<Void>> promiseList = ImmutableList.<Promise<Void>>builder();

        cleanupList.forEach(consumer -> promiseList.add(consumer.apply(context, message)));

        return Promises.when(promiseList.build()).map(voids -> null);
    }

    public Promise<Map<String, Object>> initialize(T message) {

        final ImmutableList.Builder<Promise<Map<String, Object>>> builder1 =
            ImmutableList.<Promise<Map<String, Object>>>builder();

        initializeList.forEach(function -> builder1.add(function.apply(message)));
        return Promises.allComplete(builder1.build())
            .map(
                promises -> {
                    try {

                        final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

                        promises
                            .stream()
                            .filter(
                                Promise::isSuccess)
                            .map(Promise::get)
                            .forEach(builder::putAll);

                        return builder.build();

                    } catch (Exception e) {
                        LOGGER.error("FATAL_ERROR_IN_INITIALIZER", e);
                        return Collections.EMPTY_MAP;
                    }
                }
            )
            ;
    }

    public StateLifeCycle<T, R> toImmutable() {
        return new StateLifeCycle<>(
            ImmutableList.copyOf(initializeList), ImmutableList.copyOf(executeList), ImmutableList.copyOf(cleanupList));
    }


    //Static

    //Functions
    public static <TT> List<BiFunction<Context, TT, Promise<Map<String, Object>>>> executeList() {
        List<BiFunction<Context, TT, Promise<Map<String, Object>>>> list = new ArrayList<>();
        return list;
    }

    public static <TT> List<Function<TT, Promise<Map<String, Object>>>> initializeList() {
        List<Function<TT, Promise<Map<String, Object>>>> initializers = new ArrayList<>();
        return initializers;
    }

    public static <TT> List<BiFunction<Context, TT, Promise<Void>>> cleanupList() {
        List<BiFunction<Context, TT, Promise<Void>>> cleanups = new ArrayList<>();
        return cleanups;
    }

    public static <T> BiFunction<Context, T, Promise<Map<String, Object>>> executorBuilder(
        List<BiFunction<Context, T, Promise<Map<String, Object>>>> executeList) {

        return (Context context, T message) -> {

            final ImmutableList.Builder<Promise<Map<String, Object>>> builder = ImmutableList.<Promise<Map<String, Object>>>builder();

            executeList.forEach(function -> builder.add(function.apply(context, message)));

            return Promises.when(builder.build())
                .map(maps -> {
                    final ImmutableMap.Builder<String, Object> builder1 = ImmutableMap.<String, Object>builder();

                    maps.forEach(builder1::putAll);

                    return builder1.build();
                });
        };
    }

    public static <T> BiFunction<Context, T, Promise<Void>> cleanupBuilder(List<BiFunction<Context, T, Promise<Void>>> cleanupList) {

        return (Context context, T message) -> {

            ImmutableList.Builder<Promise<Void>> promiseList = ImmutableList.<Promise<Void>>builder();

            cleanupList.forEach(consumer -> promiseList.add(consumer.apply(context, message)));

            return Promises.when(promiseList.build()).map(voids -> null);
        };
    }

    public static <T> Function<T, Promise<Map<String, Object>>> initializeBuilder(
        List<Function<T, Promise<Map<String, Object>>>> initializeList) {

        return (T message) -> {

            final ImmutableList.Builder<Promise<Map<String, Object>>> builder1 =
                ImmutableList.<Promise<Map<String, Object>>>builder();

            initializeList.forEach(function -> builder1.add(function.apply(message)));
            return Promises.allComplete(builder1.build())
                .map(
                    promises -> {
                        try {

                            final ImmutableMap.Builder<String, Object> builder = ImmutableMap.builder();

                            promises
                                .stream()
                                .filter(
                                    Promise::isSuccess)
                                .map(Promise::get)
                                .forEach(builder::putAll);

                            return builder.build();

                        } catch (Exception e) {
                            LOGGER.error("FATAL_ERROR_IN_INITIALIZER", e);
                            return Collections.EMPTY_MAP;
                        }
                    }
                )
                ;
        };
    }
}
