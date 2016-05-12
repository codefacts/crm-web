package webcomposer.statehandlers;

import com.google.common.collect.ImmutableList;
import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateCallbacksBuilder;
import io.crm.statemachine.StateMachine;
import io.crm.statemachine.StateTrigger;
import io.crm.util.Util;
import io.crm.web.util.WebUtils;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import webcomposer.MSG;
import webcomposer.MSGBuilder;
import webcomposer.Rsp;
import webcomposer.util.EventCn;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by shahadat on 5/9/16.
 */
public class FIndAllHandler {
    private final String table;

    public FIndAllHandler(String table) {
        this.table = table;
    }

    public StateCallbacks<MSG<JsonObject>, MSG<JsonObject>> toStateCallbacks() {
        return
            new StateCallbacksBuilder<MSG<JsonObject>, MSG<JsonObject>>()
                .onEnter(enter()).build();
    }

    private FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG<JsonObject>>>> enter() {
        return msg -> {
            final JsonObject body = msg.body == null ? Util.EMPTY_JSON_OBJECT : msg.body;
            final Set<String> names = body.fieldNames();
            final ImmutableList.Builder<Object> builder = ImmutableList.builder();
            final String ands = names.stream()
                .peek(name -> builder.add(body.getValue(name)))
                .map(name -> "`" + name + "`" + " = ?")
                .collect(Collectors.joining(" and "));
            final String where = ands.isEmpty() ? "" : "where " + ands;

            return WebUtils.query(
                "select * from" +
                    " " + table +
                    " " + where, new JsonArray(builder.build()), msg.connection)
                .map(
                    resultSet -> {

                        MSGBuilder<JsonObject> bldr = msg.builder();
                        return bldr
                            .setBody(
                                new JsonObject()
                                    .put(Rsp.DATA, resultSet.getRows())
                            ).build();
                    })
                .map(jsonObjectMSG -> StateMachine.trigger(EventCn.NEXT, jsonObjectMSG))
                ;
        };
    }
}
