package io.crm.web.template.page;

import io.crm.web.Uris;
import io.crm.web.template.Page;
import io.crm.web.template.page.js.BrCheckerDetailsFilterJS;
import io.vertx.core.MultiMap;
import org.watertemplate.Template;

import java.util.List;

final public class BrCheckerDetailsFilter extends Template {
    public BrCheckerDetailsFilter(final MultiMap params, final List<String> callStatuses) {
        add("script", new BrCheckerDetailsFilterJS(params, callStatuses).render());
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("/pages/br-checker-details-filter.html");
    }
}
