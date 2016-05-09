package webcomposer.statehandlers;

import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateCallbacksBuilder;
import io.crm.statemachine.StateMachine;
import io.crm.statemachine.StateTrigger;
import io.vertx.core.eventbus.EventBus;
import webcomposer.MSG;
import webcomposer.util.EventCn;

/**
 * Created by shahadat on 5/9/16.
 */
public class PublishEventHandler<T> {
    final String domainName;
    final EventBus eventBus;
    final String event;

    public PublishEventHandler(String domainName, EventBus eventBus, String event) {
        this.domainName = domainName;
        this.eventBus = eventBus;
        this.event = event;
    }

    public StateCallbacks<MSG<T>, MSG<T>> toStateCallbacks() {
        return new StateCallbacksBuilder<MSG<T>, MSG<T>>()
            .onEnter(enter(event)).build();
    }

    private FunctionUnchecked<MSG<T>, Promise<StateTrigger<MSG<T>>>> enter(final String event) {
        return msg -> {
            eventBus.publish(domainName + "/" + event, msg.body);
            return Promises.from(
                StateMachine.trigger(EventCn.NEXT, msg)
            );
        };
    }
}
