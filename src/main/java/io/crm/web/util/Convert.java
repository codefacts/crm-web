package io.crm.web.util;

import io.crm.intfs.TriFunction;
import io.crm.util.Util;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Created by shahadat on 3/30/16.
 */
final public class Convert {
    public static Builder<Integer> toInt(final int val) {
        return new IntegerBuilder(val);
    }

    public static Builder<Long> toLong(final long val) {
        return new LongBuilder(val);
    }

    public static Builder<Float> toFloat(final float val) {
        return new FloatBuilder(val);
    }

    public static Builder<Double> toDouble(final double val) {
        return new DoubleBuilder(val);
    }

    public static Builder<Date> toDate(final Date val) {
        return new DateBuilder(val);
    }

    public static Builder<Boolean> yesNoToBoolean(final boolean val) {
        return new YesNoBuilder(val);
    }

    public static Builder<Boolean> toBoolean(final boolean val) {
        return new BooleanBuilder(val);
    }

    public static Builder<String> trim(final String val) {
        return new StringBuilder(val);
    }

    public static final Builder<Object> identity(Object t) {

        return new IdentityBuilder(t);
    }

    public static Builder<JsonArray> toJsonArray(final JsonArray jsonArray) {
        return new JsonArrayBuilder(jsonArray);
    }

    public static Builder<JsonObject> toJsonObject(final JsonObject jsonObject) {
        return new JsonObjectBuilder(jsonObject);
    }

    public static interface Builder<T> {
        Function<Object, T> get();
    }

    public static final class IntegerBuilder implements Builder<Integer> {
        private final int defaultValue;
        private BiFunction<Object, Integer, Object> before;
        private BiFunction<Integer, Integer, Integer> after;
        private BiFunction<String, Integer, Integer> onEmptyWhiteSpace;
        private TriFunction<Throwable, Object, Integer, Integer> onError;
        private boolean noDefault;
        private Function<String, Integer> parser;

        public IntegerBuilder(final int val) {
            this.defaultValue = val;
        }

        public void setOnEmptyWhiteSpace(BiFunction<String, Integer, Integer> onEmptyWhiteSpace) {
            this.onEmptyWhiteSpace = onEmptyWhiteSpace;
        }

        public void setOnError(TriFunction<Throwable, Object, Integer, Integer> onError) {
            this.onError = onError;
        }

        public void setBefore(BiFunction<Object, Integer, Object> before) {
            this.before = before;
        }

        public void setAfter(BiFunction<Integer, Integer, Integer> after) {
            this.after = after;
        }

        public void setNoDefault(boolean noDefault) {
            this.noDefault = noDefault;
        }

        public void setParser(Function<String, Integer> parser) {
            this.parser = parser;
        }

        @Override
        public Function<Object, Integer> get() {

            Function<Object, Integer> function = val -> {
                val = before.apply(val, defaultValue);
                Integer convert = convert(val);
                return after.apply(convert, defaultValue);
            };

            if (onError != null) {

                return val -> {
                    try {
                        return function.apply(val);
                    } catch (Exception ex) {
                        return onError.apply(ex, val, defaultValue);
                    }
                };

            }

            return function;
        }

        public Integer convert(Object val) {

            final Integer dv = noDefault ? null : defaultValue;
            if (val == null) return dv;

            if (val instanceof Number) {
                return ((Number) val).intValue();
            } else {
                String str = val.toString();

                if (isEmptyOrWhiteSpace(str)) {
                    if (onEmptyWhiteSpace != null) {
                        return onEmptyWhiteSpace.apply(str, defaultValue);
                    } else {
                        return dv;
                    }
                } else {
                    return parser == null ? new Double(str).intValue() : parser.apply(str);
                }
            }
        }
    }

    public static final class LongBuilder implements Builder<Long> {
        private long defaultValue;
        private BiFunction<Object, Long, Object> before;
        private BiFunction<Long, Long, Long> after;
        private BiFunction<String, Long, Long> onEmptyWhiteSpace;
        private TriFunction<Throwable, Object, Long, Long> onError;
        private boolean noDefault;
        private Function<String, Long> parser;

        public LongBuilder(final long val) {
            this.defaultValue = val;
        }

        @Override
        public Function<Object, Long> get() {

            Function<Object, Long> function = val -> {
                val = before.apply(val, defaultValue);
                Long convert = convert(val);
                return after.apply(convert, defaultValue);
            };

            if (onError != null) {

                return val -> {
                    try {
                        return function.apply(val);
                    } catch (Exception ex) {
                        return onError.apply(ex, val, defaultValue);
                    }
                };

            }

            return function;
        }

        public Long convert(Object val) {
            final Long dv = noDefault ? null : defaultValue;
            if (val == null) return dv;

            if (val instanceof Number) {
                return ((Number) val).longValue();
            } else {
                String str = val.toString();

                if (isEmptyOrWhiteSpace(str)) {
                    if (onEmptyWhiteSpace != null) {
                        return onEmptyWhiteSpace.apply(str, defaultValue);
                    } else {
                        return dv;
                    }
                } else {
                    return parser == null ? new Double(str).longValue() : parser.apply(str);
                }
            }
        }

        public void setBefore(BiFunction<Object, Long, Object> before) {
            this.before = before;
        }

        public void setAfter(BiFunction<Long, Long, Long> after) {
            this.after = after;
        }

        public void setOnEmptyWhiteSpace(BiFunction<String, Long, Long> onEmptyWhiteSpace) {
            this.onEmptyWhiteSpace = onEmptyWhiteSpace;
        }

        public void setOnError(TriFunction<Throwable, Object, Long, Long> onError) {
            this.onError = onError;
        }

        public void setDefaultValue(long defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setNoDefault(boolean noDefault) {
            this.noDefault = noDefault;
        }

        public void setParser(Function<String, Long> parser) {
            this.parser = parser;
        }
    }

    public static final class FloatBuilder implements Builder<Float> {
        private float defaultValue;
        private BiFunction<Object, Float, Object> before;
        private BiFunction<Float, Float, Float> after;
        private BiFunction<String, Float, Float> onEmptyWhiteSpace;
        private TriFunction<Throwable, Object, Float, Float> onError;
        private boolean noDefault;
        private Function<String, Float> parser;

        public FloatBuilder(final float val) {
            this.defaultValue = val;
        }

        @Override
        public Function<Object, Float> get() {

            Function<Object, Float> function = val -> {
                val = before.apply(val, defaultValue);
                Float convert = convert(val);
                return after.apply(convert, defaultValue);
            };

            if (onError != null) {

                return val -> {
                    try {
                        return function.apply(val);
                    } catch (Exception ex) {
                        return onError.apply(ex, val, defaultValue);
                    }
                };

            }

            return function;
        }

        public Float convert(Object val) {
            final Float dv = noDefault ? null : defaultValue;
            if (val == null) return dv;

            if (val instanceof Number) {
                return ((Number) val).floatValue();
            } else {
                String str = val.toString();

                if (isEmptyOrWhiteSpace(str)) {
                    if (onEmptyWhiteSpace != null) {
                        return onEmptyWhiteSpace.apply(str, defaultValue);
                    } else {
                        return dv;
                    }
                } else {
                    return parser == null ? new Float(str) : parser.apply(str);
                }
            }
        }

        public void setBefore(BiFunction<Object, Float, Object> before) {
            this.before = before;
        }

        public void setAfter(BiFunction<Float, Float, Float> after) {
            this.after = after;
        }

        public void setOnEmptyWhiteSpace(BiFunction<String, Float, Float> onEmptyWhiteSpace) {
            this.onEmptyWhiteSpace = onEmptyWhiteSpace;
        }

        public void setOnError(TriFunction<Throwable, Object, Float, Float> onError) {
            this.onError = onError;
        }

        public void setDefaultValue(float defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setNoDefault(boolean noDefault) {
            this.noDefault = noDefault;
        }

        public void setParser(Function<String, Float> parser) {
            this.parser = parser;
        }
    }

    public static final class DoubleBuilder implements Builder<Double> {
        private double defaultValue;
        private BiFunction<Object, Double, Object> before;
        private BiFunction<Double, Double, Double> after;
        private BiFunction<String, Double, Double> onEmptyWhiteSpace;
        private TriFunction<Throwable, Object, Double, Double> onError;
        private boolean noDefault;
        private Function<String, Double> parser;

        public DoubleBuilder(final double val) {
            this.defaultValue = val;
        }

        @Override
        public Function<Object, Double> get() {

            Function<Object, Double> function = val -> {
                val = before.apply(val, defaultValue);
                Double convert = convert(val);
                return after.apply(convert, defaultValue);
            };

            if (onError != null) {

                return val -> {
                    try {
                        return function.apply(val);
                    } catch (Exception ex) {
                        return onError.apply(ex, val, defaultValue);
                    }
                };

            }

            return function;
        }

        public Double convert(Object val) {
            final Double dv = noDefault ? null : defaultValue;
            if (val == null) return dv;

            if (val instanceof Number) {
                return ((Number) val).doubleValue();
            } else {
                String str = val.toString();

                if (isEmptyOrWhiteSpace(str)) {
                    if (onEmptyWhiteSpace != null) {
                        return onEmptyWhiteSpace.apply(str, defaultValue);
                    } else {
                        return dv;
                    }
                } else {
                    return parser == null ? new Double(str) : parser.apply(str);
                }
            }
        }

        public void setBefore(BiFunction<Object, Double, Object> before) {
            this.before = before;
        }

        public void setAfter(BiFunction<Double, Double, Double> after) {
            this.after = after;
        }

        public void setOnEmptyWhiteSpace(BiFunction<String, Double, Double> onEmptyWhiteSpace) {
            this.onEmptyWhiteSpace = onEmptyWhiteSpace;
        }

        public void setOnError(TriFunction<Throwable, Object, Double, Double> onError) {
            this.onError = onError;
        }

        public void setDefaultValue(double defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setNoDefault(boolean noDefault) {
            this.noDefault = noDefault;
        }

        public void setParser(Function<String, Double> parser) {
            this.parser = parser;
        }
    }

    public static final class BooleanBuilder implements Builder<Boolean> {
        private boolean defaultValue;
        private BiFunction<Object, Boolean, Object> before;
        private BiFunction<Boolean, Boolean, Boolean> after;
        private BiFunction<String, Boolean, Boolean> onEmptyWhiteSpace;
        private BiFunction<Number, Boolean, Boolean> onNumberType;
        private TriFunction<Throwable, Object, Boolean, Boolean> onError;
        private boolean noDefault;
        private Function<String, Boolean> parser;

        public BooleanBuilder(final boolean val) {
            defaultValue = val;
        }

        @Override
        public Function<Object, Boolean> get() {
            Function<Object, Boolean> function = val -> {
                val = before.apply(val, defaultValue);
                Boolean convert = convert(val);
                return after.apply(convert, defaultValue);
            };

            if (onError != null) {

                return val -> {
                    try {
                        return function.apply(val);
                    } catch (Exception ex) {
                        return onError.apply(ex, val, defaultValue);
                    }
                };

            }

            return function;
        }

        public Boolean convert(Object val) {
            final Boolean dv = noDefault ? null : defaultValue;
            if (val == null) return dv;

            if (val instanceof Boolean) {
                return (Boolean) val;
            } else if (val instanceof Number) {
                return onNumberType != null ? onNumberType.apply((Number) val, defaultValue)
                    : ((Number) val).intValue() != 0;
            } else {
                String str = val.toString();

                if (isEmptyOrWhiteSpace(str)) {
                    if (onEmptyWhiteSpace != null) {
                        return onEmptyWhiteSpace.apply(str, defaultValue);
                    } else {
                        return dv;
                    }
                } else {
                    return parser == null ? Boolean.parseBoolean(str) : parser.apply(str);
                }
            }
        }

        public void setOnNumberType(BiFunction<Number, Boolean, Boolean> onNumberType) {
            this.onNumberType = onNumberType;
        }

        public void setBefore(BiFunction<Object, Boolean, Object> before) {
            this.before = before;
        }

        public void setAfter(BiFunction<Boolean, Boolean, Boolean> after) {
            this.after = after;
        }

        public void setOnEmptyWhiteSpace(BiFunction<String, Boolean, Boolean> onEmptyWhiteSpace) {
            this.onEmptyWhiteSpace = onEmptyWhiteSpace;
        }

        public void setOnError(TriFunction<Throwable, Object, Boolean, Boolean> onError) {
            this.onError = onError;
        }

        public void setDefaultValue(boolean defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setNoDefault(boolean noDefault) {
            this.noDefault = noDefault;
        }

        public void setParser(Function<String, Boolean> parser) {
            this.parser = parser;
        }
    }

    public static final class YesNoBuilder implements Builder<Boolean> {
        private boolean defaultValue;
        private BiFunction<Object, Boolean, Object> before;
        private BiFunction<Boolean, Boolean, Boolean> after;
        private BiFunction<String, Boolean, Boolean> onEmptyWhiteSpace;
        private BiFunction<Number, Boolean, Boolean> onNumberType;
        private TriFunction<Throwable, Object, Boolean, Boolean> onError;
        private boolean noDefault;
        private Function<String, Boolean> parser;

        public YesNoBuilder(final boolean val) {
            this.defaultValue = val;
        }

        @Override
        public Function<Object, Boolean> get() {

            Function<Object, Boolean> function = val -> {
                val = before.apply(val, defaultValue);
                Boolean convert = convert(val);
                return after.apply(convert, defaultValue);
            };

            if (onError != null) {

                return val -> {
                    try {
                        return function.apply(val);
                    } catch (Exception ex) {
                        return onError.apply(ex, val, defaultValue);
                    }
                };

            }

            return function;
        }

        public Boolean convert(Object val) {
            final Boolean dv = noDefault ? null : defaultValue;
            if (val == null) return dv;

            if (val instanceof Boolean) {
                return (Boolean) val;
            } else if (val instanceof Number) {
                return onNumberType != null ? onNumberType.apply((Number) val, defaultValue)
                    : ((Number) val).intValue() != 0;
            } else {
                String str = val.toString();

                if (isEmptyOrWhiteSpace(str)) {
                    if (onEmptyWhiteSpace != null) {
                        return onEmptyWhiteSpace.apply(str, defaultValue);
                    } else {
                        return dv;
                    }
                } else {
                    return parser == null ? Boolean.parseBoolean(str) : parser.apply(str);
                }
            }
        }

        public void setOnNumberType(BiFunction<Number, Boolean, Boolean> onNumberType) {
            this.onNumberType = onNumberType;
        }

        public void setBefore(BiFunction<Object, Boolean, Object> before) {
            this.before = before;
        }

        public void setAfter(BiFunction<Boolean, Boolean, Boolean> after) {
            this.after = after;
        }

        public void setOnEmptyWhiteSpace(BiFunction<String, Boolean, Boolean> onEmptyWhiteSpace) {
            this.onEmptyWhiteSpace = onEmptyWhiteSpace;
        }

        public void setOnError(TriFunction<Throwable, Object, Boolean, Boolean> onError) {
            this.onError = onError;
        }

        public void setDefaultValue(boolean defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setNoDefault(boolean noDefault) {
            this.noDefault = noDefault;
        }

        public void setParser(Function<String, Boolean> parser) {
            this.parser = parser;
        }
    }

    public static final class IdentityBuilder implements Builder<Object> {
        private Object defaultValue;
        private BiFunction<Object, Object, Object> before;
        private BiFunction<Object, Object, Object> after;
        private TriFunction<Throwable, Object, Object, Object> onError;
        private boolean noDefault;

        public IdentityBuilder(final Object t) {
            this.defaultValue = t;
        }

        @Override
        public Function<Object, Object> get() {

            Function<Object, Object> function = val -> {
                val = before.apply(val, defaultValue);
                Object convert = convert(val);
                return after.apply(convert, defaultValue);
            };

            if (onError != null) {

                return val -> {
                    try {
                        return function.apply(val);
                    } catch (Exception ex) {
                        return onError.apply(ex, val, defaultValue);
                    }
                };

            }

            return function;
        }

        public Object convert(Object val) {
            final Object dv = noDefault ? null : defaultValue;
            if (val == null) return dv;
            return val;
        }

        public void setBefore(BiFunction<Object, Object, Object> before) {
            this.before = before;
        }

        public void setAfter(BiFunction<Object, Object, Object> after) {
            this.after = after;
        }

        public void setOnError(TriFunction<Throwable, Object, Object, Object> onError) {
            this.onError = onError;
        }

        public void setDefaultValue(Object defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setNoDefault(boolean noDefault) {
            this.noDefault = noDefault;
        }


    }

    public static final class StringBuilder implements Builder<String> {
        private String defaultValue;
        private BiFunction<Object, String, Object> before;
        private BiFunction<String, String, String> after;
        private TriFunction<Throwable, Object, String, String> onError;
        private Function<Object, String> converter;
        private boolean noDefault;

        public StringBuilder(final String val) {
            this.defaultValue = val;
        }

        @Override
        public Function<Object, String> get() {

            Function<Object, String> function = val -> {
                val = before.apply(val, defaultValue);
                String convert = convert(val);
                return after.apply(convert, defaultValue);
            };

            if (onError != null) {

                return val -> {
                    try {
                        return function.apply(val);
                    } catch (Exception ex) {
                        return onError.apply(ex, val, defaultValue);
                    }
                };

            }

            return function;
        }

        public String convert(Object val) {
            final String dv = noDefault ? null : defaultValue;
            if (val == null) return dv;
            if (val instanceof String) {
                return val.toString();
            } else {
                return converter == null ? val.toString() : converter.apply(val);
            }
        }

        public void setConverter(Function<Object, String> converter) {
            this.converter = converter;
        }

        public void setBefore(BiFunction<Object, String, Object> before) {
            this.before = before;
        }

        public void setAfter(BiFunction<String, String, String> after) {
            this.after = after;
        }

        public void setOnError(TriFunction<Throwable, Object, String, String> onError) {
            this.onError = onError;
        }

        public void setDefaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setNoDefault(boolean noDefault) {
            this.noDefault = noDefault;
        }
    }

    public static final class JsonObjectBuilder implements Builder<JsonObject> {
        private JsonObject defaultValue;
        private BiFunction<Object, JsonObject, Object> before;
        private BiFunction<JsonObject, JsonObject, JsonObject> after;
        private BiFunction<String, JsonObject, JsonObject> onEmptyWhiteSpace;
        private TriFunction<Throwable, Object, JsonObject, JsonObject> onError;
        private boolean noDefault;
        private Function<String, JsonObject> parser;

        public JsonObjectBuilder(final JsonObject jsonObject) {
            this.defaultValue = jsonObject;
        }

        @Override
        public Function<Object, JsonObject> get() {

            Function<Object, JsonObject> function = val -> {
                val = before.apply(val, defaultValue);
                JsonObject convert = convert(val);
                return after.apply(convert, defaultValue);
            };

            if (onError != null) {

                return val -> {
                    try {
                        return function.apply(val);
                    } catch (Exception ex) {
                        return onError.apply(ex, val, defaultValue);
                    }
                };

            }

            return function;
        }

        public JsonObject convert(Object val) {
            final JsonObject dv = noDefault ? null : defaultValue;
            if (val == null) return dv;
            if (val instanceof JsonObject) {
                return (JsonObject) val;
            } else {
                final String string = val.toString();
                return parser == null ? new JsonObject(string) : parser.apply(string);
            }
        }

        public void setBefore(BiFunction<Object, JsonObject, Object> before) {
            this.before = before;
        }

        public void setAfter(BiFunction<JsonObject, JsonObject, JsonObject> after) {
            this.after = after;
        }

        public void setOnEmptyWhiteSpace(BiFunction<String, JsonObject, JsonObject> onEmptyWhiteSpace) {
            this.onEmptyWhiteSpace = onEmptyWhiteSpace;
        }

        public void setOnError(TriFunction<Throwable, Object, JsonObject, JsonObject> onError) {
            this.onError = onError;
        }

        public void setDefaultValue(JsonObject defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setNoDefault(boolean noDefault) {
            this.noDefault = noDefault;
        }

        public void setParser(Function<String, JsonObject> parser) {
            this.parser = parser;
        }
    }

    public static final class JsonArrayBuilder implements Builder<JsonArray> {
        private JsonArray defaultValue;
        private BiFunction<Object, JsonArray, Object> before;
        private BiFunction<JsonArray, JsonArray, JsonArray> after;
        private BiFunction<String, JsonArray, JsonArray> onEmptyWhiteSpace;
        private TriFunction<Throwable, Object, JsonArray, JsonArray> onError;
        private boolean noDefault;
        private Function<String, JsonArray> parser;

        public JsonArrayBuilder(final JsonArray jsonArray) {
            this.defaultValue = jsonArray;
        }

        @Override
        public Function<Object, JsonArray> get() {

            Function<Object, JsonArray> function = val -> {
                val = before.apply(val, defaultValue);
                JsonArray convert = convert(val);
                return after.apply(convert, defaultValue);
            };

            if (onError != null) {

                return val -> {
                    try {
                        return function.apply(val);
                    } catch (Exception ex) {
                        return onError.apply(ex, val, defaultValue);
                    }
                };

            }

            return function;
        }

        public JsonArray convert(Object val) {
            final JsonArray dv = noDefault ? null : defaultValue;
            if (val == null) return dv;
            if (val instanceof JsonObject) {
                return (JsonArray) val;
            } else {
                final String string = val.toString();
                return parser == null ? new JsonArray(string) : parser.apply(string);
            }
        }

        public void setBefore(BiFunction<Object, JsonArray, Object> before) {
            this.before = before;
        }

        public void setAfter(BiFunction<JsonArray, JsonArray, JsonArray> after) {
            this.after = after;
        }

        public void setOnEmptyWhiteSpace(BiFunction<String, JsonArray, JsonArray> onEmptyWhiteSpace) {
            this.onEmptyWhiteSpace = onEmptyWhiteSpace;
        }

        public void setOnError(TriFunction<Throwable, Object, JsonArray, JsonArray> onError) {
            this.onError = onError;
        }

        public void setDefaultValue(JsonArray defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setNoDefault(boolean noDefault) {
            this.noDefault = noDefault;
        }

        public void setParser(Function<String, JsonArray> parser) {
            this.parser = parser;
        }
    }

    public static final class DateBuilder implements Builder<Date> {
        private Date defaultValue;
        private BiFunction<Object, Date, Object> before = (o, dv) -> o;
        private BiFunction<Date, Date, Date> after = (o, dv) -> o;
        private BiFunction<String, Date, Date> onEmptyWhiteSpace;
        private TriFunction<Throwable, Object, Date, Date> onError;
        private boolean noDefault;
        private Function<String, Date> parser;
        private DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

        public DateBuilder(final Date val) {
            this.defaultValue = val;
        }

        @Override
        public Function<Object, Date> get() {

            Function<Object, Date> function = val -> {
                val = before.apply(val, defaultValue);
                Date convert = convert(val);
                return after.apply(convert, defaultValue);
            };

            if (onError != null) {

                return val -> {
                    try {
                        return function.apply(val);
                    } catch (Exception ex) {
                        return onError.apply(ex, val, defaultValue);
                    }
                };

            }

            return function;
        }

        public Date convert(Object val) {
            final Date dv = noDefault ? null : defaultValue;
            if (val == null) return dv;
            if (val instanceof Date) {
                return (Date) val;
            } else {
                final String string = val.toString();
                return parser == null ? new Date(
                    formatter.parse(string)
                        .getLong(ChronoField.INSTANT_SECONDS) * 1000
                ) : parser.apply(string);
            }
        }

        public void setFormatter(DateTimeFormatter formatter) {
            this.formatter = formatter;
        }

        public void setBefore(BiFunction<Object, Date, Object> before) {
            this.before = before;
        }

        public void setAfter(BiFunction<Date, Date, Date> after) {
            this.after = after;
        }

        public void setOnEmptyWhiteSpace(BiFunction<String, Date, Date> onEmptyWhiteSpace) {
            this.onEmptyWhiteSpace = onEmptyWhiteSpace;
        }

        public void setOnError(TriFunction<Throwable, Object, Date, Date> onError) {
            this.onError = onError;
        }

        public void setDefaultValue(Date defaultValue) {
            this.defaultValue = defaultValue;
        }

        public void setNoDefault(boolean noDefault) {
            this.noDefault = noDefault;
        }

        public void setParser(Function<String, Date> parser) {
            this.parser = parser;
        }
    }

    private static boolean isEmptyOrWhiteSpace(String str) {
        return str.trim().isEmpty();
    }
}
