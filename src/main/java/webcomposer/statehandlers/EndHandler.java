package webcomposer.statehandlers;

import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateMachine;
import io.crm.statemachine.StateTrigger;
import io.vertx.core.eventbus.Message;
import webcomposer.MSG;

/**
 * Created by shahadat on 5/8/16.
 */
public class EndHandler<T> extends StateCallbacks<MSG<T>, MSG<T>> {

    protected EndHandler() {
        super(enter(), null);
    }

    public static <T> FunctionUnchecked<MSG<T>,
        Promise<StateTrigger<MSG<T>>>>
    enter() {
        return tmsg -> {
            final Message message = tmsg.message;
            if (tmsg.deliveryOptions != null) {
                message.reply(tmsg.body, tmsg.deliveryOptions);
            } else {
                message.reply(tmsg.body);
            }
            return Promises.from(StateMachine.exit(tmsg));
        };
    }
}
