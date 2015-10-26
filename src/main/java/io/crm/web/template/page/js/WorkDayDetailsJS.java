package io.crm.web.template.page.js;

import io.crm.web.template.Page;
import org.watertemplate.Template;

/**
 * Created by someone on 26/10/2015.
 */
public class WorkDayDetailsJS extends Template {


    public WorkDayDetailsJS(final String callDetailsSummaryView) {
        add("callDetailsSummaryView", callDetailsSummaryView);
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("/pages/work-day-details.js");
    }
}
