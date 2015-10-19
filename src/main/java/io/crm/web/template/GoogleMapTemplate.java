package io.crm.web.template;

import org.watertemplate.Template;

//API KEY NAME imsl
// API KEY AIzaSyDUNDejuT59tnZhnY05VJDBBtI9_UJV_ag
public class GoogleMapTemplate extends Template {
    public GoogleMapTemplate(final String marker_title, final double lat, final double lng) {
        add("marker_title", marker_title);
        add("lat", lat + "");
        add("lng", lng + "");
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("google-map.html");
    }
}
