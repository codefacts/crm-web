package io.crm.web.template;

import io.crm.web.util.Script;
import org.watertemplate.Template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by someone on 07/09/2015.
 */
final public class PageBuilder {
    private String page_title = "";
    private Template body;
    private final Map<String, Script> scripts = new HashMap<>();

    private final Map<String, String> styles = new HashMap<>();

    private final List<String> hiddens = new ArrayList<>();

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

    public PageBuilder addBabelScript(final String name, final String src) {
        scripts.put(name, new Script(name, src, Script.Type.Babel));
        return this;
    }

    public PageBuilder addScript(final String name, final String src) {
        scripts.put(name, new Script(name, src, Script.Type.JavaScript));
        return this;
    }

    public PageBuilder addHidden(String hidden) {
        hiddens.add(hidden);
        return this;
    }

    public Page build() {
        return new Page(page_title, body, scripts.values(), styles.values(), hiddens);
    }
}
