package io.crm.web.template;

import org.watertemplate.Template;

/**
 * Created by someone on 03/10/2015.
 */
public class JavascriptRedirect extends Template {
    public JavascriptRedirect(String redirectUri) {
        add("redirectUri", redirectUri);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("javascript-redirect.html");
    }
}
