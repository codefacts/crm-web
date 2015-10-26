package io.crm.web.template.page;

import io.crm.web.template.Page;
import org.watertemplate.Template;
import org.watertemplate.TemplateMap;

/**
 * Created by someone on 23/09/2015.
 */
public class ReactDOMBinder extends Template {

    public ReactDOMBinder(final String script) {
        add("script", script);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("reac-dom-binder.html");
    }
}
