package io.crm.web.template.input;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 30/09/2015.
 */
public class DateInputTemplate extends Template {
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

    public DateInputTemplate(String id, String classes, String name, Date value, String placeholder) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        add("name", getOrDefault(name, ""));
        add("value", value == null ? "" : DATE_FORMAT.format(value));
        add("placeholder", getOrDefault(placeholder, ""));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("date.html");
    }
}
