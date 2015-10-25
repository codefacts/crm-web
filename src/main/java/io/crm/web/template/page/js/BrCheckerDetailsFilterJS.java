package io.crm.web.template.page.js;

import io.crm.util.Util;
import io.crm.web.Uris;
import io.crm.web.template.Page;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import org.watertemplate.Template;

import java.util.Arrays;
import java.util.List;

/**
 * Created by someone on 25/10/2015.
 */
public class BrCheckerDetailsFilterJS extends Template {
    public BrCheckerDetailsFilterJS(final MultiMap params, final List<String> callStatuses) {
        add("searchClusterUrl", Uris.searchCluster.value);
        add("searchTsrCodeUrl", Uris.searchTsrCode.value);
        add("searchAuditorCodeUrl", Uris.searchAuditorCode.value);
        add("searchConsumerNameUrl", Uris.searchConsumerName.value);
        add("searchConsumerMobileUrl", Uris.searchConsumerMobile.value);
        final List<String> paramsList = Arrays.asList("CLUSTER_NAME", "TSR_CODE", "AUDITOR_CODE",
                "CONSUMER_NAME", "CONSUMER_MOBILE_NUMBER", "CALL_NO_RANGE",
                "TOTAL_VISITED_RANGE", "BAND", "CONTACTED", "NAME_MATCH",
                "CALL_STATUS", "DATE_AND_TIME_RANGE", "BAND", "CONTACTED", "NAME_MATCH", "CALL_STATUS"
        );
        paramsList.forEach(name -> add(name, Util.getOrDefault(params.get(name), "")));
        addCollection("callStatuses", callStatuses);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("/pages/br-checker-details-filter.js");
    }
}
