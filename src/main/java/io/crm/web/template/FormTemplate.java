package io.crm.web.template;

import io.crm.util.Util;
import io.vertx.core.json.JsonObject;
import org.watertemplate.Template;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 30/09/2015.
 */
public class FormTemplate extends Template {

    public FormTemplate(String id, String classes, final Collection<RowTemplate> rowTemplates) {
        add("id", getOrDefault(id, ""));
        add("class", classes);
        addCollection("rows", rowTemplates.stream().map(r -> r.render()).collect(Collectors.toList()), (row, m) -> {
            m.add("row", row);
        });
    }

    @Override
    protected String getFilePath() {
        return "form.html";
    }
}
