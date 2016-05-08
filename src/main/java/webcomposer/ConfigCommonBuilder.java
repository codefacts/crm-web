package webcomposer;

import io.crm.util.DataTypes;
import io.vertx.core.json.JsonObject;

import java.util.Map;
import java.util.function.Function;

public class ConfigCommonBuilder {
    private Map<Integer, String> jdbc_types;
    private Map<Integer, DataTypes> data_types_map;
    private Map<Integer, Function<Object, Object>> type_converters;
    private Map<Integer, JsonObject> error_codes_map;

    public ConfigCommonBuilder setJdbc_types(Map<Integer, String> jdbc_types) {
        this.jdbc_types = jdbc_types;
        return this;
    }

    public ConfigCommonBuilder setData_types_map(Map<Integer, DataTypes> data_types_map) {
        this.data_types_map = data_types_map;
        return this;
    }

    public ConfigCommonBuilder setType_converters(Map<Integer, Function<Object, Object>> type_converters) {
        this.type_converters = type_converters;
        return this;
    }

    public ConfigCommonBuilder setError_codes_map(Map<Integer, JsonObject> error_codes_map) {
        this.error_codes_map = error_codes_map;
        return this;
    }

    public ConfigCommon createConfigCommon() {
        return ConfigCommon.create(jdbc_types, data_types_map, type_converters, error_codes_map);
    }
}