package webcomposer.statehandlers;

import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateCallbacksBuilder;
import io.crm.statemachine.StateMachine;
import io.crm.statemachine.StateTrigger;
import webcomposer.MSG;
import webcomposer.MSGBuilder;
import webcomposer.util.EventCn;

/**
 * Created by shahadat on 5/8/16.
 */
public class StartHandler<T> {

    public StartHandler() {
    }

    private FunctionUnchecked<MSG<T>, Promise<StateTrigger<MSG<T>>>> enter() {

        return msg -> {
            final MSGBuilder<T> builder = msg.builder();

            return Promises.from(
                StateMachine.trigger(
                    EventCn.NEXT,
                    builder.setBody(msg.body).build()));
        };
    }

    public StateCallbacks<MSG<T>, MSG<T>> toStateCallbacks() {
        return
            new StateCallbacksBuilder<MSG<T>, MSG<T>>()
                .onEnter(enter()).build();
    }
}
