package io.crm.web.service.callreview;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Defer;
import io.crm.util.ExceptionUtil;
import io.crm.util.Util;
import io.crm.web.ST;
import io.crm.web.service.callreview.entity.BrCheckerData;
import io.crm.web.service.callreview.model.BrCheckerModel;
import io.crm.web.service.callreview.repository.BrCheckerDataRepository;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by someone on 20/10/2015.
 */
@Service
public class BrCheckerJsonService {
    private final ObjectMapper objectMapper;
    private JDBCClient jdbcClient;
    private Vertx vertx;
    private final BrCheckerDataRepository repository;
    private final DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");

    @Autowired
    public BrCheckerJsonService(final BrCheckerDataRepository repository) {
        this.repository = repository;
        objectMapper = new ObjectMapper();
        objectMapper.setDateFormat(dateFormat);
    }

    public void initialize(final Vertx vertx) {
        this.vertx = vertx;
        jdbcClient = JDBCClient.createShared(vertx, new JsonObject()
                        .put("driver_class", "com.microsoft.sqlserver.jdbc.SQLServerDriver")
                        .put("url", BrCheckerDetailsService.CONNECTION_URL)
                        .put("user", "sa")
                        .put("password", "sqladmin@123")
        );
    }

    public void searchCluster(final Message<JsonObject> message) {
        ExceptionUtil.withReplyRun(() -> {
            final Defer<JsonObject> defer = Promises.defer();

            vertx.executeBlocking((Future<JsonObject> f) -> {
                final JsonObject criteria = message.body();
                final Page<String> page = repository.searchClusterName(criteria);
                final List<String> list = page.getContent();
                final List<JsonObject> objectList = list
                        .stream()
                        .map(v -> new JsonObject().put(BrCheckerModel.CLUSTER_NAME.name(), v))
                        .collect(Collectors.toList());

                f.complete(
                        new JsonObject()
                                .put(ST.data, objectList)
                                .put(ST.pagination, new JsonObject()
                                        .put(ST.page, page.getNumber() + 1)
                                        .put(ST.numberOfElements, page.getNumberOfElements())
                                        .put(ST.size, page.getSize())
                                        .put(ST.totalPages, page.getTotalPages())
                                        .put(ST.total, page.getTotalElements()))
                );
            }, Util.makeDeferred(defer));

            defer.promise()
                    .success(page ->
                            message.reply(page))
                    .error(e ->
                            ExceptionUtil.fail(message, e));

        }, message);
    }

    public static void main(String... args) {

    }
}
