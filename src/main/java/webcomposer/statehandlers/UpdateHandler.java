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
import webcomposer.Cnst;
import webcomposer.DomainInfo;
import webcomposer.MSG;
import webcomposer.util.EventCn;

import java.util.List;

/**
 * Created by shahadat on 5/9/16.
 */
public class UpdateHandler {
    final DomainInfo domainInfo;
    final IncludeExcludeTransformation includeExcludeTransformation;

    public UpdateHandler(List<String> fields, DomainInfo domainInfo) {
        this.domainInfo = domainInfo;

        this.includeExcludeTransformation = new IncludeExcludeTransformation(ImmutableSet.copyOf(fields), null);
    }

    private FunctionUnchecked<MSG<JsonObject>, Promise<StateTrigger<MSG<Object>>>> enter() {
        return msg -> {

            final JsonObject transform = includeExcludeTransformation.transform(msg.body);
            final Long id = transform.getLong(Cnst.ID);

            return WebUtils.update(domainInfo.plural, transform,
                new JsonObject()
                    .put(Cnst.ID, id), msg.connection)
                .map(updateResult -> StateMachine.trigger(EventCn.CREATE_SUCCESS,
                    msg.builder().setBody(updateResult).build()))
                ;
        };
    }

    public StateCallbacks<MSG<JsonObject>, MSG<Object>> toStateCallbacks() {
        return new StateCallbacksBuilder<MSG<JsonObject>, MSG<Object>>()
            .onEnter(enter()).build();
    }
}
