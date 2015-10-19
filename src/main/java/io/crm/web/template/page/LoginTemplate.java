package io.crm.web.template.page;

import io.crm.web.template.Page;
import org.watertemplate.Template;

/**
 * Created by someone on 21/09/2015.
 */
public class LoginTemplate extends Template {
    @Override
    protected String getFilePath() {
        return Page.templatePath("login-form.html");
    }
}
