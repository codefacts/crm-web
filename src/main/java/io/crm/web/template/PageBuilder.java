package io.crm.web.template;

import org.watertemplate.Template;

/**
 * Created by someone on 07/09/2015.
 */
public class PageBuilder {
    private String page_title = "";
    private Template body;

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

    public Page build() {
        return new Page(page_title, body);
    }
}
