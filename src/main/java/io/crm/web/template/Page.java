package io.crm.web.template;

import io.crm.util.Util;
import io.crm.web.App;
import org.watertemplate.Template;
import org.watertemplate.TemplateMap;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;

import static io.crm.web.template.TemplateUtil.EMPTY_TEMPLATE;

/**
 * Created by someone on 07/09/2015.
 */
public class Page extends Template {
    private static final String templateDir = App.loadConfig().getString("templateDir");
    private final String page_title;
    private final Template body;

    Page(final String page_title, final Template body, final Collection<String> headerScripts, final Collection<String> footerScripts, final Collection<String> styles) {
        this.page_title = page_title;
        this.body = body;
        add("page_title", page_title);
        addCollection("headerScripts", headerScripts);
        addCollection("footerScripts", footerScripts);
        addCollection("styles", styles);
    }

    @Override
    protected void addSubTemplates(TemplateMap.SubTemplates subTemplates) {
        subTemplates.add("body", Util.getOrDefault(body, EMPTY_TEMPLATE));
    }

    @Override
    protected String getFilePath() {
        return templatePath("page.html");
    }

    public static String templatePath(String filename) {

        return "file:" + Paths.get(templateDir, filename);
    }
}
