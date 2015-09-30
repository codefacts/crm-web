package io.crm.web.template.input;

import io.crm.util.Util;
import org.watertemplate.Template;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 30/09/2015.
 */
public class SelectTemplate extends Template {

    public SelectTemplate(String id, String classes, String name, Map<String, String> valuesMap) {
        add("id", getOrDefault(id, ""));
        add("class", getOrDefault(classes, ""));
        add("name", getOrDefault(name, ""));
        final Set<Map.Entry<String, String>> options = valuesMap != null ? valuesMap.entrySet() : Collections.EMPTY_MAP.entrySet();
        addCollection("options", options, (op, map) -> {
            map.add("id", op.getKey());
            map.add("value", op.getValue());
        });
    }

    @Override
    protected String getFilePath() {
        return "select.html";
    }
}
