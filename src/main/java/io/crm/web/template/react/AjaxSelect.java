package io.crm.web.template.react;

import io.crm.web.template.Page;
import org.watertemplate.Template;

/**
 * Created by someone on 20/10/2015.
 */
public class AjaxSelect extends Template {
    @Override
    protected String getFilePath() {
        return Page.templatePath("ajax-select.html");
    }
}
