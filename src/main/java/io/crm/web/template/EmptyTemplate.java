package io.crm.web.template;

import org.watertemplate.Template;

/**
 * Created by someone on 07/09/2015.
 */
final public class EmptyTemplate extends Template {
    @Override
    protected String getFilePath() {
        return Page.templatePath("empty.html");
    }
}
