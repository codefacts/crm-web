package io.crm.web.template.input;

import org.watertemplate.Template;

import java.util.Collections;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 30/09/2015.
 */
public class RangeTemplate extends Template {

    public RangeTemplate(String id, String classes, String name, Number from, Number to, String placeHolderFrom, String placeHolderTo) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        add("name", getOrDefault(name, ""));
        add("from", from == null ? "" : from.toString());
        add("to", to == null ? "" : to.toString());
        addMappedObject("placeholder", Collections.EMPTY_LIST, m -> {
            m.add("from", getOrDefault(placeHolderFrom, ""));
            m.add("to", getOrDefault(placeHolderTo, ""));
        });
    }

    @Override
    protected String getFilePath() {
        return "range.html";
    }
}
