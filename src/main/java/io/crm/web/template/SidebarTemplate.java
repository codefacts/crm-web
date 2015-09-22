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
                        .put(ST.uri, Uris.dashboard.value)
                        .put(ST.label, Uris.dashboard.label),
                new JsonObject()
                        .put(ST.uri, Uris.callDetails.value)
                        .put(ST.label, Uris.callDetails.label)
        );
    }

    public SidebarTemplate(final String currentUri) {
        links.forEach(link -> {
            if (currentUri.equals(link.getString(ST.uri))) {
                link.put(ST.active, "active");
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
