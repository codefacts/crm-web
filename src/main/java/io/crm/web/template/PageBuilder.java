package io.crm.web.template;

import org.watertemplate.Template;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by someone on 07/09/2015.
 */
final public class PageBuilder {
    private String page_title = "";
    private Template body;
    private final Map<String, String> headerScripts = new HashMap<>();
    private final Map<String, String> footerScripts = new HashMap<>();

    private final Map<String, String> styles = new HashMap<>();

    public PageBuilder(String page_title) {
        this.page_title = page_title;
    }

    public static PageBuilder create(String page_title) {
        final PageBuilder pageBuilder = new PageBuilder(page_title);
        return pageBuilder;
    }

    public PageBuilder body(Template body) {
        this.body = body;
        return this;
    }

    public PageBuilder addStyle(final String name, final String href) {
        styles.put(name, href);
        return this;
    }

    public PageBuilder addHeaderScript(final String name, final String src) {
        headerScripts.put(name, src);
        return this;
    }

    public PageBuilder addFooterScript(final String name, final String src) {
        footerScripts.put(name, src);
        return this;
    }

    public Page build() {
        return new Page(page_title, body, headerScripts.values(), footerScripts.values(), styles.values());
    }
}
