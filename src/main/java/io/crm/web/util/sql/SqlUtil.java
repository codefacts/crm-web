package io.crm.web.util.sql;

import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by shahadat on 2/14/16.
 */
final public class SqlUtil {

    public static List<JsonObject> params(Stream<JsonObject> stream) {
        List<JsonObject> params = stream
            .map(j ->
                new JsonObject()
                    .put("Name", "@" + j.getString("name").replace('.', '_'))
                    .put("Value", j.getValue("value")))
            .collect(Collectors.toList());
        return params;
    }

    public static String eq(Stream<JsonObject> stream) {
        String where = stream.map(j -> j.getString("name"))
            .map(n -> n + " = @" + n.replace('.', '_')).collect(Collectors.joining(" AND ")).trim();
        return where;
    }
}
