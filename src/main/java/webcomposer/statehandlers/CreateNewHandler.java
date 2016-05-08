package webcomposer.statehandlers;

import com.google.common.collect.ImmutableSet;
import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateMachine;
import io.crm.statemachine.StateTrigger;
import io.crm.transformation.impl.json.object.IncludeExcludeTransformation;
import io.crm.web.util.WebUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sql.UpdateResult;
import webcomposer.DomainInfo;
import webcomposer.MSG;
import webcomposer.util.EventCn;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by shahadat on 5/8/16.
 */
public class CreateNewHandler extends StateCallbacks<MSG<JsonObject>, MSG<Object>> {

    protected CreateNewHandler(FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG<Object>>>> onEnter, Callable<Promise<Void>> onExit, CreateNewHandler.StateHolder stateHolder) {
        super(enter(stateHolder), onExit);
    }

    private static FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG<Object>>>> enter(StateHolder stateHolder) {
        return msg -> {

            final JsonObject transform = stateHolder.includeExcludeTransformation.transform(msg.body);

            return WebUtils.create(stateHolder.domainInfo.plural, transform, msg.connection)
                .map(UpdateResult::getKeys)
                .map(jsonArray -> jsonArray.getLong(0))
                .map(id -> StateMachine.trigger(EventCn.CREATE_SUCCESS, msg.builder().setBody(id).build()))
                ;
        };
    }

    public static class StateHolder {

        final DomainInfo domainInfo;
        final IncludeExcludeTransformation includeExcludeTransformation;

        public StateHolder(List<String> fields, DomainInfo domainInfo) {
            this.domainInfo = domainInfo;

            this.includeExcludeTransformation = new IncludeExcludeTransformation(ImmutableSet.copyOf(fields), null);
        }
    }
}
