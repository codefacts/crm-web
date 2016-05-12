package webcomposer;

import com.google.common.collect.ImmutableMap;
import io.crm.ErrorCodes;
import io.crm.util.DataTypes;
import io.crm.util.ExceptionUtil;
import io.crm.web.util.Converters;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by shahadat on 3/27/16.
 */
public class ConfigCommon {
    public static final Logger LOGGER = LoggerFactory.getLogger(ConfigCommon.class);

    public static final String SIZE = "size";
    public static final String DATA = "data";
    public static final String ID = "id";

    public static final String ERROR_CODE = "errorCode";
    public static final String HTTP_RESPONSE_CODE = "httpResponseCode";
    public static final String MESSAGE_CODE = "messageCode";

    public final Map<Integer, String> JDBC_TYPES;
    public final Map<Integer, DataTypes> DATA_TYPES_MAP;
    public final Map<Integer, Function<Object, Object>> TYPE_CONVERTERS;
    public final Map<Integer, JsonObject> ERROR_CODES_MAP;

    private ConfigCommon(
        Map<Integer, String> jdbc_types,
        Map<Integer, DataTypes> data_types_map,
        Map<Integer, Function<Object, Object>> type_converters,
        Map<Integer, JsonObject> error_codes_map) {
        JDBC_TYPES = jdbc_types;
        DATA_TYPES_MAP = data_types_map;
        TYPE_CONVERTERS = type_converters;
        ERROR_CODES_MAP = error_codes_map;
    }

    public static ConfigCommon create(
        Map<Integer, String> jdbc_types,
        Map<Integer, DataTypes> data_types_map,
        Map<Integer, Function<Object, Object>> type_converters,
        Map<Integer, JsonObject> error_codes_map) {
        return new ConfigCommon(jdbc_types, data_types_map, type_converters, error_codes_map);
    }

    public static Map<Integer, String> jdbcTypes() {
        return ImmutableMap.copyOf(Arrays.asList(Types.class.getDeclaredFields())
            .stream()
            .filter(fs -> Modifier.isFinal(fs.getModifiers()) && Modifier.isPublic(fs.getModifiers())
                && Modifier.isStatic(fs.getModifiers()))
            .collect(Collectors.toMap(
                fs ->
                    ExceptionUtil.toRuntimeCall(() -> fs.getInt(null)),
                ExceptionUtil.toRuntimeCall(() -> Field::getName))));
    }

    public static ImmutableMap<Integer, DataTypes> dataTypesMap() {
        ImmutableMap.Builder<Integer, DataTypes> builder = ImmutableMap.builder();
        builder
            .put(4, DataTypes.LONG)
            .put(12, DataTypes.STRING)
            .put(-1, DataTypes.STRING)
            .put(-7, DataTypes.BOOLEAN)
            .put(91, DataTypes.DATE)
            .put(93, DataTypes.DATE)
            .put(8, DataTypes.DATE.DOUBLE)
        ;
        return builder.build();
    }

    public static ImmutableMap<Integer, Function<Object, Object>> typeConverters() {
        ImmutableMap.Builder<Integer, Function<Object, Object>> builder = ImmutableMap.builder();
        builder
            .put(4, Converters::toLong)
            .put(12, s -> s)
            .put(-1, s -> s)
            .put(-7, Converters::toBoolean)
            .put(91, Converters::toMySqlDateString)
            .put(93, Converters::toMySqlDateString)
            .put(8, Converters::toDouble)
        ;

        return builder.build();
    }

    public static ImmutableMap<Integer, JsonObject> errorCodesMap(Iterator<ErrorRsp> iterator) {
        ImmutableMap.Builder<Integer, JsonObject> builder = ImmutableMap.builder();

        Arrays.asList(ErrorCodes.values())
            .forEach(ec -> builder.put(ec.code(),
                new JsonObject()
                    .put(ERROR_CODE, ec.code())
                    .put(MESSAGE_CODE, ec.messageCode())
                    .put(HTTP_RESPONSE_CODE, ec.httpResponseCode())));

        iterator.forEachRemaining(
            ec ->
                builder.put(ec.code,
                    new JsonObject()
                        .put(ERROR_CODE, ec.code)
                        .put(MESSAGE_CODE, ec.messageCode)
                        .put(HTTP_RESPONSE_CODE, ec.httpResponseCode)));

        return builder.build();
    }

    public ImmutableMap<String, Function<Object, Object>> converters(String[] fields, String tableName, JsonObject dbConfig) {
        String url = dbConfig.getString("url");
        String user = dbConfig.getString("user");
        String password = dbConfig.getString("password");

        ImmutableMap.Builder<String, Function<Object, Object>> builder = ImmutableMap.builder();
        try {
            try (Connection connection = DriverManager.getConnection(url, user, password)) {
                Statement statement = connection.createStatement();
                statement.execute("select * from " + tableName);
                ResultSet rs = statement.getResultSet();
                ResultSetMetaData metaData = rs.getMetaData();

                int columnCount = metaData.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    int columnType = metaData.getColumnType(i + 1);
                    Function<Object, Object> converter = TYPE_CONVERTERS.get(columnType);
                    Objects.requireNonNull(converter, "Type Converter can't be null for Type: " +
                        "[" + fields[i] + " " + columnType + ": " + JDBC_TYPES.get(columnType) + "]");
                    builder.put(fields[i], converter);
                    System.out.println(columnType + ": " + JDBC_TYPES.get(columnType));
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error connecting to database through jdbc", e);
        }

        return builder.build();
    }
}
