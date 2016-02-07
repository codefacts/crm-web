package io.crm.web.template.input;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by someone on 30/09/2015.
 */
public class SelectTemplate extends Template {

    public SelectTemplate(String id, String classes, String name, Map<String, String> valuesMap, String selected) {
        add("id", Util.or(id, ""));
        add("class", Util.or(classes, ""));
        add("name", Util.or(name, ""));
        final Set<Map.Entry<String, String>> options = valuesMap != null ? valuesMap.entrySet() : Collections.EMPTY_MAP.entrySet();
        addCollection("options", options, (op, map) -> {
            map.add("id", op.getKey());
            map.add("selected", Util.or(selected, "").equals(op.getKey()) ? "selected" : "");
            map.add("value", op.getValue());
        });
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("select.html");
    }
}
