package webcomposer.statehandlers;

import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateCallbacksBuilder;
import io.crm.statemachine.StateMachine;
import io.crm.statemachine.StateTrigger;
import io.crm.web.util.WebUtils;
import io.vertx.core.json.JsonObject;
import webcomposer.Cnst;
import webcomposer.MSG;
import webcomposer.util.EventCn;

/**
 * Created by shahadat on 5/9/16.
 */
public class DeleteHandler<T> {
    final String table;

    public DeleteHandler(String table) {
        this.table = table;
    }

    public StateCallbacks<MSG<T>, MSG<T>> toStateCallbacks() {
        return
            new StateCallbacksBuilder<MSG<T>, MSG<T>>()
                .onEnter(enter()).build();
    }

    private FunctionUnchecked<MSG<T>, Promise<StateTrigger<MSG<T>>>> enter() {
        return
            msg -> WebUtils.delete(table,
                new JsonObject()
                    .put(Cnst.ID, msg.body), msg.connection)
                .map(updateResult -> StateMachine.trigger(EventCn.NEXT, msg));
    }
}
