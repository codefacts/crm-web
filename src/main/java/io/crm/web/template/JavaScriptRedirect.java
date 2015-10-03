package io.crm.web.template;

import org.watertemplate.Template;

/**
 * Created by sohan on 10/2/2015.
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
