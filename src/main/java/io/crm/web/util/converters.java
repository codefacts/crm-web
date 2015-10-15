package io.crm.web.util;

import io.crm.util.Util;

import static io.crm.util.Util.isEmptyOrNullOrSpaces;

/**
 * Created by someone on 12/10/2015.
 */
final public class Converters {
    public static int toInt(final String val) {
        if (isEmptyOrNullOrSpaces(val)) return 0;
        return (int) Double.parseDouble(val);
    }

    public static long toLong(final String val) {
        if (isEmptyOrNullOrSpaces(val)) return 0L;
        return (long) Double.parseDouble(val);
    }

    public static float toFloat(final String val) {
        if (isEmptyOrNullOrSpaces(val)) return 0.0F;
        return Float.parseFloat(val);
    }

    public static double toDouble(final String val) {
        if (isEmptyOrNullOrSpaces(val)) return 0.0;
        return Double.parseDouble(val);
    }

    public static boolean yesNoToBoolean(final String val) {
        if (val == null) {
            return false;
        }
        return val.equalsIgnoreCase("Yes") ? true : false;
    }

    public static boolean toBoolean(final String val) {
        if (val == null) return false;
        return Boolean.parseBoolean(val);
    }
}
