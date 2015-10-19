package io.crm.web.template;

import org.watertemplate.Template;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by someone on 19/10/2015.
 */
final public class TemplateList extends Template {
    TemplateList(Collection<Template> templates) {
        addCollection("templates", templates.stream().map(template -> template.render()).collect(Collectors.toList()));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("template-list.html");
    }
}
