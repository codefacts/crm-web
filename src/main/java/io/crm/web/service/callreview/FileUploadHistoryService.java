package io.crm.web.service.callreview;

import io.crm.util.ExceptionUtil;
import io.crm.util.Util;
import io.crm.web.controller.FileUploadController;
import io.crm.web.service.callreview.model.FileUploads;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;

import static io.crm.util.ExceptionUtil.toRuntime;

/**
 * Created by someone on 15/10/2015.
 */
public class FileUploadHistoryService {
    public static final String insertSQL = "INSERT INTO dbo.FileUploads (filename, uploaded_filename, upload_date, status) " +
            "VALUES (?, ?, ?, ?);";
    public static final String updateSql = "UPDATE FileUploads SET status = ?, errorDetails = ? WHERE filename = ?";

    public void insert(Message<JsonArray> message) {
        ExceptionUtil.withReplyRun(() -> {
            final Properties properties = new Properties();
            properties.setProperty("user", "sa");
            properties.setProperty("password", "sqladmin@123");
            try (final Connection connection = DriverManager.getConnection(BrCheckerDetailsService.CONNECTION_URL, properties)) {
                final List<JsonObject> list = message.body().getList();
                connection.setAutoCommit(false);
                final PreparedStatement statement = connection.prepareStatement(insertSQL);
                list.forEach(u -> {
                    toRuntime(() -> {
                        int idx = 1;
                        statement.setString(idx++, u.getString(FileUploads.filename));
                        statement.setString(idx++, u.getString(FileUploads.uploaded_filename));
                        statement.setTimestamp(idx++, new Timestamp(
                                FileUploadController.dateFormat.parse(u.getString(FileUploads.upload_date)).getTime()
                        ));
                        statement.setString(idx++, u.getString(FileUploads.status));
                        statement.addBatch();
                    });
                });
                statement.executeBatch();
                connection.commit();
                message.reply(null);
            }
        }, message);
    }

    public void update(Message<JsonArray> message) {
        ExceptionUtil.withReplyRun(() -> {
            final Properties properties = new Properties();
            properties.setProperty("user", "sa");
            properties.setProperty("password", "sqladmin@123");
            try (final Connection connection = DriverManager.getConnection(BrCheckerDetailsService.CONNECTION_URL, properties)) {
                final List<JsonObject> list = message.body().getList();
                connection.setAutoCommit(false);
                final PreparedStatement statement = connection.prepareStatement(updateSql);
                list.forEach(u -> {
                    toRuntime(() -> {
                        int idx = 1;
                        statement.setString(idx++, u.getString(FileUploads.status, ""));
                        statement.setString(idx++, u.getString(FileUploads.errorDetails, ""));
                        statement.setString(idx++, u.getString(FileUploads.filename));
                        statement.addBatch();
                    });
                });
                statement.executeBatch();
                connection.commit();
                message.reply(null);
            }
        }, message);
    }
}
