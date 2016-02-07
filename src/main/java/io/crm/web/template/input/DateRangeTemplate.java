package io.crm.web.template.input;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

import java.util.Date;

/**
 * Created by someone on 30/09/2015.
 */
public class DateRangeTemplate extends Template {

    public DateRangeTemplate(String id, String classes, String name, Date from, Date to, String placeholderFrom, String placeholderTo) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        add("name", Util.or(name, ""));
        add("from", from == null ? "" : DateInputTemplate.DATE_FORMAT.format(from));
        add("to", to == null ? "" : DateInputTemplate.DATE_FORMAT.format(to));
        addMappedObject("placeholder", new Object(), m -> {
            m.add("from", Util.or(placeholderFrom, ""));
            m.add("to", Util.or(placeholderTo, ""));
        });
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("date-range.html");
    }
}
