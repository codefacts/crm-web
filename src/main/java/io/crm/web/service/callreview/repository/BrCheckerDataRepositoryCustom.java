package io.crm.web.service.callreview.repository;

import io.crm.web.service.callreview.entity.BrCheckerData;
import io.vertx.core.json.JsonObject;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Created by someone on 21/10/2015.
 */
public interface BrCheckerDataRepositoryCustom {
    public Page<String> searchClusterName(final JsonObject criteria);

    Page<Object[]> searchTSRCode(JsonObject criteria);

    Page<Object[]> searchAuditorCode(JsonObject criteria);

    Page<String> searchConsumerName(JsonObject criteria);

    Page<String> searchConsumerMobile(JsonObject criteria);

    List<String> findAllCallStatuses();
}
