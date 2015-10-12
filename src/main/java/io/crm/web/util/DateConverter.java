package io.crm.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by someone on 12/10/2015.
 */
final public class DateConverter {
    private final DateFormat format;

    public DateConverter(final String format) {
        this.format = new SimpleDateFormat(format);
    }

    public Date toDate(final String val) {
        try {
            return format.parse(val);
        } catch (final ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
