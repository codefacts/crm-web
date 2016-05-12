package webcomposer.statehandlers;

import com.google.common.collect.ImmutableSet;
import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.intfs.Promise;
import io.crm.statemachine.StateCallbacks;
import io.crm.statemachine.StateCallbacksBuilder;
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

/**
 * Created by shahadat on 5/8/16.
 */
public class CreateNewHandler {

    final DomainInfo domainInfo;
    final IncludeExcludeTransformation includeExcludeTransformation;

    public CreateNewHandler(List<String> fields, DomainInfo domainInfo) {
        this.domainInfo = domainInfo;

        this.includeExcludeTransformation = new IncludeExcludeTransformation(ImmutableSet.copyOf(fields), null);
    }

    private FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG<Object>>>> enter() {
        return msg -> {

            final JsonObject transform = includeExcludeTransformation.transform(msg.body);

            return WebUtils.create(domainInfo.table, transform, msg.connection)
                .map(UpdateResult::getKeys)
                .map(jsonArray -> jsonArray.getLong(0))
                .map(id -> StateMachine.trigger(EventCn.NEXT, msg.builder().setBody(id).build()))
                ;
        };
    }

    public StateCallbacks<MSG<JsonObject>, MSG<Object>> toStateCallbacks() {
        return new StateCallbacksBuilder<MSG<JsonObject>, MSG<Object>>()
            .onEnter(enter()).build();
    }
}
