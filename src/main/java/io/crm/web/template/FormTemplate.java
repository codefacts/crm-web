package io.crm.web.template;

import io.crm.util.Util;
import org.watertemplate.Template;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by someone on 30/09/2015.
 */
public class FormTemplate extends Template {

    public FormTemplate(String id, String classes, final Collection<Template> rowTemplates) {
        add("id", Util.or(id, ""));
        add("class", classes);
        addCollection("rows", rowTemplates.stream().map(r -> r.render()).collect(Collectors.toList()), (row, m) -> {
            m.add("row", row);
        });
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("form.html");
    }
}
