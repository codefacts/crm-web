package io.crm.web.template;

import io.crm.web.WebST;
import io.crm.web.WebUris;
import io.vertx.core.json.JsonObject;
import org.watertemplate.Template;

import java.util.Arrays;
import java.util.List;

/**
 * Created by someone on 23/09/2015.
 */
public class SidebarTemplate extends Template {
    private final List<JsonObject> links;

    {
        links = Arrays.asList(
                new JsonObject()
                        .put(WebST.uri, WebUris.fileUpload.value)
                        .put(WebST.label, WebUris.fileUpload.label),
                new JsonObject()
                        .put(WebST.uri, WebUris.imageUpload.value)
                        .put(WebST.label, WebUris.imageUpload.label),
                new JsonObject()
                        .put(WebST.uri, WebUris.br_checker_details.value)
                        .put(WebST.label, WebUris.br_checker_details.label)
        );
    }

    SidebarTemplate(final String currentUri) {
        links.forEach(link -> {
            if (currentUri.equals(link.getString(WebST.uri))) {
                link.put(WebST.active, "list-group-item-success");
            }
        });

        addCollection("links", links, (link, m) -> {
            m.add(WebST.uri, link.getString(WebST.uri));
            m.add(WebST.label, link.getString(WebST.label));
            m.add(WebST.active, link.getString(WebST.active));
        });
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("sidebar.html");
    }
}
