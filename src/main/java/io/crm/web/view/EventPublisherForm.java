package io.crm.web.view;

import io.crm.util.Util;
import org.watertemplate.Template;

import java.util.Collection;
import java.util.stream.Collectors;

import static io.crm.util.Util.getOrDefault;

/**
 * Created by someone on 07/09/2015.
 */
public class EventPublisherForm extends Template {

    public EventPublisherForm(Collection<String> destinations, String selected, String header, String body, String reply) {
        add("header", getOrDefault(header, ""));
        add("body", getOrDefault(body, ""));
        addCollection("destinations", destinations.stream().sorted().collect(Collectors.toList()), (dest, map) -> {
            map.add("label", dest);
            map.add("selected", dest.equals(selected) ? "selected=\"\"" : "");
        });
        add("reply", getOrDefault(reply, ""));
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("event-publish-form.html");
    }
}