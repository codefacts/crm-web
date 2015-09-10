package io.crm.web.view;

import io.crm.util.Util;
import org.watertemplate.Template;
import org.watertemplate.TemplateMap;

import java.nio.file.Paths;

/**
 * Created by someone on 07/09/2015.
 */
public class Page extends Template {
    private final String page_title;
    private final Template body;

    Page(final String page_title, final Template body) {
        this.page_title = page_title;
        this.body = body;
        add("page_title", page_title);
    }

    @Override
    protected void addSubTemplates(TemplateMap.SubTemplates subTemplates) {
        subTemplates.add("body", Util.getOrDefault(body, Templates.EMPTY));
    }

    @Override
    protected String getFilePath() {
        return templatePath("page.html");
    }

    public static String templatePath(String filename) {
        return filename;
    }
}
