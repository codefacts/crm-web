package webcomposer;

import io.crm.statemachine.StateMachine;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.web.Router;

import java.util.List;
import java.util.Map;

public class AppComposerBuilder {
    private Map<DomainInfo, Map<String, StateMachine>> stateMachinesByDomainInfo;
    private Router router;
    private List<DomainInfo> domainInfoList;
    private JDBCClient jdbcClient;
    private EventBus eventBus;

    public AppComposerBuilder setStateMachinesByDomainInfo(Map<DomainInfo, Map<String, StateMachine>> stateMachinesByDomainInfo) {
        this.stateMachinesByDomainInfo = stateMachinesByDomainInfo;
        return this;
    }

    public AppComposerBuilder setRouter(Router router) {
        this.router = router;
        return this;
    }

    public AppComposerBuilder setDomainInfoList(List<DomainInfo> domainInfoList) {
        this.domainInfoList = domainInfoList;
        return this;
    }

    public AppComposerBuilder setJdbcClient(JDBCClient jdbcClient) {
        this.jdbcClient = jdbcClient;
        return this;
    }

    public AppComposerBuilder setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
        return this;
    }

    public AppComposer createAppComposer() {
        return new AppComposer(stateMachinesByDomainInfo, router, domainInfoList, jdbcClient, eventBus);
    }
}