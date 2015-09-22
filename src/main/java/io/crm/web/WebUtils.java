package io.crm.web;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Session;

/**
 * Created by someone on 22/09/2015.
 */
final public class WebUtils {
    public static boolean isLoggedIn(final Session session) {
        return session.get(ST.currentUser) != null;
    }

    public static void redirect(final String uri, final HttpServerResponse response) {
        response.setStatusCode(HttpResponseStatus.TEMPORARY_REDIRECT.code())
                .headers().set(HttpHeaders.LOCATION, uri);
        response.end();
    }
}
