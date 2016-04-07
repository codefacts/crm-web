package io.crm.web.util;

import io.crm.util.ExceptionUtil;
import io.crm.util.Util;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.Date;

import static io.crm.util.Util.isEmptyOrNullOrSpaces;

/**
 * Created by someone on 12/10/2015.
 */
final public class Converters {
    private static final ThreadLocal<DateFormat> DATE_FORMAT_THREAD_LOCAL = new ThreadLocal<DateFormat>() {
        @Override
        public DateFormat get() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static int toInt(final Object val) {
        if (val == null) return 0;
        if (val instanceof Number) {
            return ((Number) val).intValue();
        } else {
            String str = val.toString();
            if (emptyOrSpace(str)) return 0;
            return (int) Double.parseDouble(str);
        }
    }

    public static long toLong(final Object val) {
        if (val == null) return 0;
        if (val instanceof Number) {
            return ((Number) val).longValue();
        } else {
            String str = val.toString();
            if (emptyOrSpace(str)) return 0L;
            return (long) Double.parseDouble(str);
        }
    }

    public static float toFloat(final Object val) {
        if (val == null) return 0;
        if (val instanceof Number) {
            return ((Number) val).floatValue();
        } else {
            String str = val.toString();
            if (emptyOrSpace(str)) return 0.0F;
            return Float.parseFloat(str);
        }
    }

    public static double toDouble(final Object val) {
        if (val == null) return 0;
        if (val instanceof Number) {
            return ((Number) val).doubleValue();
        } else {
            String str = val.toString();
            if (emptyOrSpace(str)) return 0.0;
            return Double.parseDouble(str);
        }
    }

    public static Date toDate(final Object val) {
        if (val == null) return null;
        if (val instanceof Date) {
            return (Date) val;
        } else {
            String str = val.toString();
            if (emptyOrSpace(str)) return null;
            return new Date(DateTimeFormatter.ISO_INSTANT
                .parse(str).getLong(ChronoField.INSTANT_SECONDS) * 1000);
        }
    }

    public static boolean yesNoToBoolean(final Object val) {
        if (val == null) return false;
        String str = val.toString();
        if (str == null) {
            return false;
        }
        return str.trim().equalsIgnoreCase("Yes");
    }

    public static boolean toBoolean(final Object val) {
        if (val == null) return false;
        if (val instanceof Boolean) {
            return (Boolean) val;
        } else {
            String str = val.toString();
            return Boolean.parseBoolean(str);
        }
    }

    public static String trim(final String val) {
        return Util.or(val, "").trim();
    }

    public static final <T> T identity(T t) {
        return t;
    }

    public static JsonArray toJsonArray(final Object jsonStrng) {
        if (jsonStrng instanceof JsonArray) {
            return (JsonArray) jsonStrng;
        }
        return new JsonArray(jsonStrng.toString());
    }

    public static JsonObject toJsonObject(final Object jsonStrng) {
        if (jsonStrng instanceof JsonObject) {
            return (JsonObject) jsonStrng;
        }
        return new JsonObject(jsonStrng.toString());
    }

    public static boolean emptyOrSpace(String str) {
        return str.trim().isEmpty();
    }

    public static String toMySqlDateString(Object o) {
        if (o == null) return null;
        String isoStr = o.toString();
        Date date = new Date(DateTimeFormatter.ISO_INSTANT.parse(isoStr).getLong(ChronoField.INSTANT_SECONDS) * 1000);
        return DATE_FORMAT_THREAD_LOCAL.get().format(date);
    }
}
