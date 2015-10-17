package io.crm.web.service.callreview;

import io.crm.FailureCode;
import io.crm.util.ExceptionUtil;
import io.crm.web.App;
import io.crm.web.ST;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpClient;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import static io.crm.util.ExceptionUtil.toRuntime;
import static io.crm.util.ExceptionUtil.withReplyRun;

/**
 * Created by sohan on 10/3/2015.
 */
public class BrCheckerDetailsService {
    public static final int apiPort = App.loadConfig().getJsonObject(BrCheckerDetailsService.class.getSimpleName(), new JsonObject()).getInteger("apiPort");
    public static final String apiHost = App.loadConfig().getJsonObject(BrCheckerDetailsService.class.getSimpleName()).getString("apiHost");
    public static final String CONNECTION_URL = App.loadConfig().getJsonObject(BrCheckerDetailsService.class.getSimpleName()).getString("CONNECTION_URL");
    public static final String DATE_FORMAT_STR = "dd-MMM-yyyy hh:mm:ss";
    private final HttpClient httpClient;
    private static final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_STR);

    public BrCheckerDetailsService(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void insert(final Message<JsonObject> message) {
        final JsonArray list = message.body().getJsonArray(ST.body);
        withReplyRun(() -> {
            final Properties properties = new Properties();
            properties.setProperty("user", "sa");
            properties.setProperty("password", "sqladmin@123");
            try (final Connection connection = DriverManager.getConnection(CONNECTION_URL, properties)) {

                String sql = "INSERT INTO dbo.br_checker_entrys " +
                        "(" +
                        "TRACKER_TABLE_ID, " +
                        "CALL_NO, " +

                        "CONSUMER_NAME, " +
                        "CONSUMER_MOBILE_NUMBER, " +
                        "CALL_STATUS, " +

                        "NAME_MATCH, " +
                        "CONTACTED, " +
                        "BAND, " +

                        "TIME_OF_CHECK, " +

                        "DATE_AND_TIME, " +

                        "AUDITOR_NAME, " +
                        "AUDITOR_CODE, " +
                        "CLUSTER_NAME, " +

                        "TSR_CODE, " +
                        "TSR_NAME, " +
                        "TOTAL_VISITED, " +

                        "REMARK, " +
                        "PICTURE_NAME, " +
                        "PICTURE_URL, " +

                        "Latitude, " +
                        "Longitude, " +
                        "Accuracy, " +

                        "ORIGINAL_DATE_STRING" +
                        ") " +
                        "VALUES " +
                        "(" +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?, " +
                        "?" +
                        ")";

                connection.setAutoCommit(false);
                final PreparedStatement statement = connection.prepareStatement(sql);

                list.forEach(ar -> {
                    final JsonArray array = (JsonArray) ar;
                    toRuntime(() -> {
                        int idx = 0;
                        int p = 1;

                        idx++;
//                        p++;
                        statement.setInt(p++, array.getInteger(idx++));
                        statement.setInt(p++, array.getInteger(idx++));

                        statement.setString(p++, array.getString(idx++));
                        statement.setString(p++, array.getString(idx++));
                        statement.setString(p++, array.getString(idx++));

                        statement.setBoolean(p++, array.getBoolean(idx++));
                        statement.setBoolean(p++, array.getBoolean(idx++));
                        statement.setBoolean(p++, array.getBoolean(idx++));

                        statement.setString(p++, array.getString(idx++));

                        final String dateStr = array.getString(idx++);
                        statement.setTimestamp(p++, new Timestamp(dateFormat.parse(removeAMPM(dateStr)).getTime()));

                        idx++;
//                        p++;

                        statement.setString(p++, array.getString(idx++));
                        statement.setString(p++, array.getString(idx++));
                        statement.setString(p++, array.getString(idx++));

                        statement.setString(p++, array.getString(idx++));
                        statement.setString(p++, array.getString(idx++));
                        statement.setInt(p++, array.getInteger(idx++));

                        statement.setString(p++, array.getString(idx++));
                        statement.setString(p++, array.getString(idx++));
                        statement.setString(p++, array.getString(idx++));

                        statement.setFloat(p++, array.getFloat(idx++));
                        statement.setFloat(p++, array.getFloat(idx++));
                        statement.setFloat(p++, array.getFloat(idx++));

                        statement.setString(p++, dateStr);

                        statement.addBatch();
                    });
                });

                try {
                    final int count = statement.executeBatch().length;
                    if (count != list.size()) {
                        connection.rollback();
                        message.fail(FailureCode.InternalServerError.code, "Error in inserting value into database.");
                        return;
                    }
                } catch (Exception ex) {
                    connection.rollback();
                    message.fail(FailureCode.InternalServerError.code, "Error inserting value into database.");
                    return;
                }

                connection.commit();
                message.reply(list.size());
            }
        }, message);
    }

    private String removeAMPM(String dateStr) {
        return dateStr.replace("AM", "")
                .replace("PM", "")
                .trim();
    }

    public void findOne(final Message<Integer> message) {
        httpClient.get(apiPort, apiHost, "/BrChecker/findOne?id=" + message.body())
                .exceptionHandler(e -> ExceptionUtil.fail(message, e))
                .handler(res -> {
                    res
                            .bodyHandler(buffer -> {
                                message.reply(new JsonObject(buffer.toString()));
                            })
                            .exceptionHandler(e -> ExceptionUtil.fail(message, e))
                    ;
                })
                .end();
    }

    public void brCheckerData(final Message<JsonObject> message) {
        withReplyRun(() -> {
            int page = message.body().getInteger(ST.page, 1);
            int size = message.body().getInteger(ST.size, 20);

            httpClient
                    .get(apiPort, apiHost, String.format("/BrChecker/details?page=%d&size=%d", page, size))
                    .exceptionHandler(e -> ExceptionUtil.fail(message, e))
                    .handler(res -> {
                        res
                                .bodyHandler(buffer -> {
                                    message.reply(new JsonObject(buffer.toString()));
                                })
                                .exceptionHandler(e -> ExceptionUtil.fail(message, e))
                        ;
                    })
                    .end();
        }, message);
    }

    public static String baseUrl() {
        return "http://" + apiHost + ":" + apiPort;
    }

    public static void main(String... args) throws Exception {
        System.out.println(dateFormat.parse("04-Oct-2015 11:11"));
    }
}
