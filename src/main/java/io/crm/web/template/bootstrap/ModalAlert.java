package io.crm.web.template.bootstrap;

import io.crm.util.Util;
import io.crm.web.template.Page;
import org.watertemplate.Template;

/**
 * Created by someone on 13/10/2015.
 */
final public class ModalAlert extends Template {
    ModalAlert(String id, final String clasz, final String title, final String body, boolean show) {
        id = Util.or(id, "modal-alert");
        add("id", id);
        add("class", Util.or(clasz, ""));
        add("title", Util.or(title, ""));
        add("body", Util.or(body, ""));
        add("script", show ? showScript(id) : "");
    }

    private String showScript(final String id) {
        return "<script>\n" +
                "    $('#" + id + "').modal('show');\n" +
                "</script>";
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("model-alert.html");
    }
}
