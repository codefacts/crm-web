package io.crm.web.template.input;

import org.watertemplate.Template;

import java.util.Date;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 30/09/2015.
 */
public class DateRangeTemplate extends Template {

    public DateRangeTemplate(String id, String classes, String name, Date from, Date to, String placeholderFrom, String placeholderTo) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        add("name", getOrDefault(name, ""));
        add("from", from == null ? "" : DateInputTemplate.DATE_FORMAT.format(from));
        add("to", to == null ? "" : DateInputTemplate.DATE_FORMAT.format(to));
        addMappedObject("placeholder", new Object(), m -> {
            m.add("from", getOrDefault(placeholderFrom, ""));
            m.add("to", getOrDefault(placeholderTo, ""));
        });
    }

    @Override
    protected String getFilePath() {
        return "date-range.html";
    }
}
