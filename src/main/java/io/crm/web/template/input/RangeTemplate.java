package io.crm.web.template.input;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

import java.util.Collections;

/**
 * Created by someone on 30/09/2015.
 */
public class RangeTemplate extends Template {

    public RangeTemplate(String id, String classes, String name, Number from, Number to, String placeHolderFrom, String placeHolderTo) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        add("name", Util.or(name, ""));
        add("from", from == null ? "" : from.toString());
        add("to", to == null ? "" : to.toString());
        addMappedObject("placeholder", Collections.EMPTY_LIST, m -> {
            m.add("from", Util.or(placeHolderFrom, ""));
            m.add("to", Util.or(placeHolderTo, ""));
        });
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("range.html");
    }
}
