package io.crm.web.template.input;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by someone on 30/09/2015.
 */
public class DateInputTemplate extends Template {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

    public DateInputTemplate(String id, String classes, String name, Date value, String placeholder) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        add("name", Util.or(name, ""));
        add("value", value == null ? "" : DATE_FORMAT.format(value));
        add("placeholder", Util.or(placeholder, ""));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("date.html");
    }
}
