package io.crm.web.template;

import io.crm.web.ST;
import io.crm.web.Uris;
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
                        .put(ST.uri, Uris.fileUpload.value)
                        .put(ST.label, Uris.fileUpload.label),
                new JsonObject()
                        .put(ST.uri, Uris.imageUpload.value)
                        .put(ST.label, Uris.imageUpload.label),
                new JsonObject()
                        .put(ST.uri, Uris.br_checker_details.value)
                        .put(ST.label, Uris.br_checker_details.label)
        );
    }

    SidebarTemplate(final String currentUri) {
        links.forEach(link -> {
            if (currentUri.equals(link.getString(ST.uri))) {
                link.put(ST.active, "list-group-item-success");
            }
        });

        addCollection("links", links, (link, m) -> {
            m.add(ST.uri, link.getString(ST.uri));
            m.add(ST.label, link.getString(ST.label));
            m.add(ST.active, link.getString(ST.active));
        });
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("sidebar.html");
    }
}
