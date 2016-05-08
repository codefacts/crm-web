package webcomposer.statehandlers;

import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateTrigger;
import io.vertx.core.json.JsonObject;
import webcomposer.MSG;

import java.util.concurrent.Callable;

/**
 * Created by shahadat on 5/8/16.
 */
public class CreateNewHandler extends StateCallbacks<MSG<JsonObject>, MSG<Object>> {
    protected CreateNewHandler(FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG<Object>>>> onEnter, Callable<Promise<Void>> onExit) {
        super(enter(), onExit);
    }

    private static FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG<Object>>>> enter() {
        return msg -> {
            return null;
        };
    }
}
