package webcomposer.statehandlers;

import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateCallbacksBuilder;
import io.crm.statemachine.StateMachine;
import io.crm.statemachine.StateTrigger;
import io.crm.web.util.WebUtils;
import io.vertx.core.json.JsonObject;
import webcomposer.MSG;
import webcomposer.MSGBuilder;
import webcomposer.util.EventCn;

/**
 * Created by shahadat on 5/9/16.
 */
public class FindHandler {
    final String table;

    public FindHandler(String table) {
        this.table = table;
    }

    public StateCallbacks<MSG<Object>, MSG<JsonObject>> toStateCallbacks() {
        return
            new StateCallbacksBuilder<MSG<Object>, MSG<JsonObject>>()
                .onEnter(enter()).build();
    }

    private FunctionUnchecked<MSG<Object>, Promise<StateTrigger<MSG<JsonObject>>>> enter() {
        return
            msg -> WebUtils.query("select * from " + table + " where id = " + msg.body, msg.connection)
                .map(resultSet -> resultSet.getRows().get(0))
                .map(jsonObject -> {
                    final MSGBuilder<JsonObject> builder = msg.builder();
                    return StateMachine.trigger(EventCn.NEXT, builder.setBody(jsonObject).build());
                });
    }
}
