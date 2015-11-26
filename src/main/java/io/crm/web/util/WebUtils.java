package io.crm.web.util;

import io.crm.intfs.ConsumerUnchecked;
import io.crm.util.ExceptionUtil;
import io.crm.util.Util;
import io.crm.web.ST;
import io.crm.web.css.bootstrap.BootstrapCss;
import io.crm.web.template.pagination.PaginationItemTemplateBuilder;
import io.crm.web.template.pagination.PaginationTemplateBuilder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static io.crm.util.Util.apply;

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

    public static String titleWithTotal(final String label, final long total) {
        return String.format(label + " [%d data found]", total);
    }

    public static <T> Handler<AsyncResult<T>> catchHandler(final ConsumerUnchecked<AsyncResult<T>> consumer, final RoutingContext context) {
        return r -> {
            try {
                consumer.accept(r);
            } catch (Exception e) {
                context.fail(e);
            }
        };
    }

    public static Handler<RoutingContext> webHandler(final ConsumerUnchecked<RoutingContext> consumer) {
        return context -> {
            try {
                consumer.accept(context);
            } catch (Exception e) {
                context.fail(e);
            }
        };
    }

    public static int parseInt(final String value, int defaultValue) {
        try {
            return value == null ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static String toQueryString(MultiMap multiMap) {
        final StringBuilder builder = new StringBuilder();
        multiMap.forEach(e -> {
            builder
                    .append(ExceptionUtil.toRuntimeCall(() -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8.name())))
                    .append("=")
                    .append(ExceptionUtil.toRuntimeCall(() -> URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8.name())))
                    .append("&")
            ;
        });
        if (builder.length() > 0) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    public static PaginationTemplateBuilder createPaginationTemplateBuilder(final String uriPath, final String queryString, final Pagination pagination, final int paginationNavLength) {
        int size = pagination.getSize();
        return new PaginationTemplateBuilder()
                .addClass(BootstrapCss.PULL_RIGHT.value)
                .first(uriPath + pageQueryString(pagination.first(), size, queryString), pagination.isFirst())
                .prev(uriPath + pageQueryString(pagination.prev(), size, queryString), pagination.hasPrev())
                .addAllItems(items -> {
                    pagination.nav(paginationNavLength).forEach(p -> items.add(
                            new PaginationItemTemplateBuilder()
                                    .setLabel("" + p)
                                    .setHref(uriPath + pageQueryString(p, size, queryString))
                                    .addClass(pagination.isCurrentPage(p) ? BootstrapCss.ACTIVE.value : "")
                                    .createPaginationItemTemplate()
                    ));
                })
                .next(uriPath + pageQueryString(pagination.next(), size, queryString), pagination.hasNext())
                .last(uriPath + pageQueryString(pagination.last(), size, queryString), pagination.isLast());
    }

    private static String pageQueryString(final int prev, final int size, final String queryString) {
        return String.format("?page=%d&size=%d&%s", prev, size, queryString);
    }

    public static int rangify(final int val, final int min, final int max) {
        return val < min ? min : val > max ? max : val;
    }
}
