package io.crm.web.util;

import com.google.common.collect.ImmutableList;
import io.crm.intfs.ConsumerUnchecked;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Defer;
import io.crm.promise.intfs.Promise;
import io.crm.sql.SqlUtils;
import io.crm.util.DataTypes;
import io.crm.util.ExceptionUtil;
import io.crm.util.Util;
import io.crm.util.touple.immutable.Tpl2;
import io.crm.util.touple.immutable.Tpls;
import io.crm.web.ST;
import io.crm.web.css.bootstrap.BootstrapCss;
import io.crm.web.template.pagination.PaginationItemTemplateBuilder;
import io.crm.web.template.pagination.PaginationTemplateBuilder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.ext.sql.UpdateResult;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.crm.util.Util.accept;
import static java.util.stream.Collectors.*;

/**
 * Created by someone on 22/09/2015.
 */
final public class WebUtils {
    private static final String DATE = "date";
    private static final String DATETIME = "datetime";
    private static final String TIME = "time";
    private static final String TIMESTAMP = "timestamp";
    private static final String ID = "id";
    private static final String FIELD = "field";
    private static final String LABEL = "label";
    private static final String IS_KEY = "isKey";
    private static final String DATA_TYPE = "dataType";

    public static boolean isLoggedIn(final Session session) {
        return session.get(ST.currentUser) != null;
    }

    public static void redirect(final String uri, final HttpServerResponse response) {
        response.setStatusCode(HttpResponseStatus.TEMPORARY_REDIRECT.code())
            .headers().set(HttpHeaders.LOCATION, uri);
        response.end();
    }

    public static String titleWithTotal(final String label, final long total) {
        return String.format(label + " [%d data found]", total);
    }

    public static <T> Handler<AsyncResult<T>> catchHandler(final ConsumerUnchecked<AsyncResult<T>> consumer, final RoutingContext context) {
        return r -> {
            try {
                consumer.accept(r);
            } catch (Exception e) {
                context.fail(e);
            }
        };
    }

    public static Handler<RoutingContext> webHandler(final ConsumerUnchecked<RoutingContext> consumer) {
        return context -> {
            try {
                consumer.accept(context);
            } catch (Exception e) {
                context.fail(e);
            }
        };
    }

    public static int parseInt(final String value, int defaultValue) {
        try {
            return value == null ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String toQueryString(MultiMap multiMap) {
        final StringBuilder builder = new StringBuilder();
        multiMap.forEach(e -> {
            builder
                .append(ExceptionUtil.toRuntimeCall(() -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8.name())))
                .append("=")
                .append(ExceptionUtil.toRuntimeCall(() -> URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8.name())))
                .append("&")
            ;
        });
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static PaginationTemplateBuilder createPaginationTemplateBuilder(final String uriPath, final String queryString, final Pagination pagination, final int paginationNavLength) {
        int size = pagination.getSize();
        return new PaginationTemplateBuilder()
            .addClass(BootstrapCss.PULL_RIGHT.value)
            .first(uriPath + pageQueryString(pagination.first(), size, queryString), pagination.isFirst())
            .prev(uriPath + pageQueryString(pagination.prev(), size, queryString), pagination.hasPrev())
            .addAllItems(items -> {
                pagination.nav(paginationNavLength).forEach(p -> items.add(
                    new PaginationItemTemplateBuilder()
                        .setLabel("" + p)
                        .setHref(uriPath + pageQueryString(p, size, queryString))
                        .addClass(pagination.isCurrentPage(p) ? BootstrapCss.ACTIVE.value : "")
                        .createPaginationItemTemplate()
                ));
            })
            .next(uriPath + pageQueryString(pagination.next(), size, queryString), pagination.hasNext())
            .last(uriPath + pageQueryString(pagination.last(), size, queryString), pagination.isLast());
    }

    public static JsonObject pageSize(HttpServerRequest request) {
        final JsonObject requestJson = new JsonObject()
            .put(ST.page, Converters.toInt(request.params().get(ST.page)))
            .put(ST.size, Converters.toInt(request.params().get(ST.size)));
        return requestJson;
    }

    private static String pageQueryString(final int prev, final int size, final String queryString) {
        return String.format("?page=%d&size=%d&%s", prev, size, queryString);
    }

    public static int rangify(final int val, final int min, final int max) {
        return val < min ? min : val > max ? max : val;
    }

    public static JsonObject toJson(final MultiMap params) {
        final JsonObject criteria = new JsonObject();
        params.names().forEach(name -> {
            if (params.getAll(name).size() <= 1) {
                criteria.put(name, params.get(name));
            } else {
                criteria.put(name, new JsonArray(params.getAll(name)));
            }
        });
        return criteria;
    }

    public static Tpl2<String, String> splitRange(String string, String regex) {
        final String[] split = string.split(regex, 2);
        if (split.length > 1) {
            return Tpls.of(split[0], split[1]);
        } else {
            return Tpls.of(split[0], "");
        }
    }

    public static void main(String args[]) {
        System.out.println(toTitle(""));
    }

    public static String toTitle(String src) {
        Pattern pattern = Pattern.compile("");
        String str = Stream.of(src).map(s -> s.replace('_', ' ')).map(s -> s.split(" "))
            .flatMap(strings -> Arrays.asList(strings).stream())
            .map(s -> splitCamelCase(s, " "))
            .flatMap(s -> Arrays.asList(s.split(" ")).stream())
            .map(String::trim)
            .map(s -> s.toCharArray())
            .map(chars -> accept(chars, chs -> {
                if (chs.length > 0)
                    chs[0] = Character.toUpperCase(chs[0]);
            }))
            .map(chars -> new String(chars))
            .collect(joining(" "));
        ;
        return str;
    }

    public static String splitCamelCase(String s, String replace) {
        String regex = "([a-z])([A-Z])";
        String replacement = "$1" + replace + "$2";
        return s.replaceAll(regex, replacement);
    }

    public static Promise<ResultSet> query(String sql, JDBCClient jdbcClient) {
        return getConnection(jdbcClient)
            .mapToPromise(con -> Promises.from(con).mapToPromise(cn -> {
                Defer<ResultSet> defer = Promises.defer();
                cn.query(sql, Util.makeDeferred(defer));
                return defer.promise();
            }).complete(p -> con.close()));
    }

    public static Promise<ResultSet> query(String sql, JsonArray params, JDBCClient jdbcClient) {
        return getConnection(jdbcClient)
            .mapToPromise(conn -> Promises.from(conn).mapToPromise(con -> {
                Defer<ResultSet> defer = Promises.defer();
                con.queryWithParams(sql, params, Util.makeDeferred(defer));
                return defer.promise();
            }).complete(p -> conn.close()));
    }

    public static Promise<UpdateResult> update(String sql, JDBCClient jdbcClient) {
        return getConnection(jdbcClient)
            .mapToPromise(con -> Promises.from(con).mapToPromise(cn -> {
                Defer<UpdateResult> defer = Promises.defer();
                cn.update(sql, Util.makeDeferred(defer));
                return defer.promise();
            }).complete(p -> con.close()));
    }

    public static Promise<UpdateResult> updateWithParams(String sql, JsonArray params, SQLConnection sqlConnection) {
        return Promises.from(sqlConnection)
            .mapToPromise(conn -> Promises.from(conn).mapToPromise(con -> {
                Defer<UpdateResult> defer = Promises.defer();
                con.updateWithParams(sql, params, Util.makeDeferred(defer));
                return defer.promise();
            }));
    }

    public static Promise<UpdateResult> updateWithParams(String sql, JsonArray params, JDBCClient jdbcClient) {
        return getConnection(jdbcClient)
            .mapToPromise(conn -> Promises.from(conn).mapToPromise(con -> {
                Defer<UpdateResult> defer = Promises.defer();
                con.updateWithParams(sql, params, Util.makeDeferred(defer));
                return defer.promise();
            }).complete(p -> conn.close()));
    }

    public static Promise<UpdateResult> create(String table, JsonObject params, SQLConnection sqlConnection) {
        return WebUtils.updateWithParams(
            SqlUtils.create(table, params.fieldNames()),
            new JsonArray(params.getMap().values().stream().collect(toList())), sqlConnection);
    }

    public static Promise<UpdateResult> update(String tableName, JsonObject object, JsonObject where, SQLConnection con) {

        final StringBuilder builder = new StringBuilder();
        final String e = "`";
        builder.append("UPDATE ").append(e).append(tableName).append(e)
            .append(" set ");

        final JsonArray params = new JsonArray();

        Set<String> fieldNames = object.fieldNames();

        final String QQ = ", ";
        fieldNames.forEach(name -> {
            builder.append(e).append(name).append(e).append(" = ").append("?").append(QQ);
            params.add(object.getValue(name));
        });

        builder.delete(builder.lastIndexOf(QQ), builder.length());

        builder.append(" where ");

        Set<String> fieldNames2 = where.fieldNames();

        fieldNames2.forEach(name -> {
            builder.append(e).append(name).append(e).append(" = ").append("?").append(QQ);
            params.add(where.getValue(name));
        });

        builder.delete(builder.lastIndexOf(QQ), builder.length());

        return updateWithParams(builder.toString(), params, con);
    }

    public static Promise<UpdateResult> create(String table, JsonObject params, JDBCClient jdbcClient) {
        return WebUtils.updateWithParams(
            SqlUtils.create(table, params.fieldNames()),
            new JsonArray(params.getMap().values().stream().collect(toList())), jdbcClient);
    }

    public static Promise<UpdateResult> update(String table, JsonObject params, JsonObject where, JDBCClient jdbcClient) {
        StringBuilder builder = new StringBuilder();

        builder
            .append("update ")
            .append(table)
            .append(" set ")
        ;

        final String COMMA = ", ";
        params.fieldNames().forEach(name -> builder.append(name).append(" = ?").append(COMMA));

        builder.delete(builder.lastIndexOf(COMMA), builder.length());

        builder
            .append(" ")
            .append("where ")
        ;

        where.fieldNames().forEach(name -> builder.append(name).append(" = ?").append(COMMA));

        builder.delete(builder.lastIndexOf(COMMA), builder.length());

        JsonArray cond1 = new JsonArray(params.getMap().values().stream().collect(toList()));
        JsonArray cond2 = new JsonArray(where.getMap().values().stream().collect(toList()));

        return updateWithParams(builder.toString(), cond1.addAll(cond2), jdbcClient);
    }

    public static Promise<UpdateResult> update(String table, JsonObject params, Object id, JDBCClient jdbcClient) {
        return update(table, params, new JsonObject().put(ID, id), jdbcClient);
    }

    public static Promise<SQLConnection> getConnection(JDBCClient jdbcClient) {
        Defer<SQLConnection> defer = Promises.defer();
        jdbcClient.getConnection(Util.makeDeferred(defer));
        return defer.promise();
    }

    public static String attachmentFilename(String filename) {
        return "attachment; filename=" + filename + ";";
    }

    public static int offset(int page, int size) {
        return (page - 1) * size;
    }

    public static boolean inferDateTypeFromTitle(String title) {

        String lowerCase = title.toLowerCase();
        return lowerCase.endsWith(DATE)
            || lowerCase.endsWith(DATETIME)
            || lowerCase.endsWith(TIME)
            || lowerCase.endsWith(TIMESTAMP);
    }

    public static Promise<UpdateResult> delete(final String tableName, final Object id, JDBCClient jdbcClient) {
        return update("delete from " + tableName + " where id = " + id, jdbcClient);
    }

    public static JsonObject describeField(String field) {

        final String title = Util.parseCamelOrSnake(field);

        JsonObject jsonObject = new JsonObject()
            .put(FIELD, field)
            .put(LABEL, title);

        {
            if (field.equals("id")) {
                jsonObject.put(IS_KEY, true);
            }
        }

        {
            if (WebUtils.inferDateTypeFromTitle(title)) {
                jsonObject.put(DATA_TYPE, DataTypes.DATE);
            }
        }

        return jsonObject;
    }

    public static Promise<UpdateResult> createMulti(String tableName, List<JsonObject> list, SQLConnection con) {

        Set<String> fieldNames = list.stream().findFirst().orElse(Util.EMPTY_JSON_OBJECT).fieldNames();
        String sql = "insert into `" + tableName + "` (" + fieldNames.stream()
            .map(name -> "`" + name + "`")
            .collect(joining(", ")) + ") " +
            "values ";

        final String QQ = ", ";
        final StringBuilder builder = new StringBuilder();
        for (int line = 0; line < list.size(); line++) {

            builder
                .append("(")
            ;

            for (int i = 0; i < fieldNames.size(); i++) {
                builder.append("?").append(QQ);
            }

            builder.delete(builder.lastIndexOf(QQ), builder.length());

            builder
                .append(")")
                .append(QQ)
            ;

        }

        builder.delete(builder.lastIndexOf(QQ), builder.length());

        ImmutableList.Builder<Object> bldr = ImmutableList.builder();
        list.forEach(js -> bldr.addAll(fieldNames.stream().map(js::getValue).collect(toList())));

        return WebUtils.updateWithParams(sql + builder.toString(), new JsonArray(bldr.build()), con);
    }

    public static Promise<UpdateResult> delete(String tableName, JsonObject where, SQLConnection con) {

        final StringBuilder builder = new StringBuilder();
        final JsonArray params = new JsonArray();
        final String e = "`";
        final String QQ = ", ";
        builder.append("DELETE FROM `").append(tableName).append("` WHERE ");
        Set<String> fieldNames = where.fieldNames();
        fieldNames.forEach(name -> {
            builder.append(e).append(name).append(e).append(" = ?").append(QQ);
            params.add(where.getValue(name));
        });

        builder.delete(builder.lastIndexOf(QQ), builder.length());

        return WebUtils.updateWithParams(builder.toString(), params, con);
    }
}
