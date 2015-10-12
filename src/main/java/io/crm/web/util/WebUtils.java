package io.crm.web.util;

import io.crm.intfs.ConsumerInterface;
import io.crm.web.WebST;
import io.crm.web.css.bootstrap.BootstrapCss;
import io.crm.web.template.pagination.PaginationItemTemplateBuilder;
import io.crm.web.template.pagination.PaginationTemplate;
import io.crm.web.template.pagination.PaginationTemplateBuilder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

/**
 * Created by someone on 22/09/2015.
 */
final public class WebUtils {
    public static boolean isLoggedIn(final Session session) {
        return session.get(WebST.currentUser) != null;
    }

    public static void redirect(final String uri, final HttpServerResponse response) {
        response.setStatusCode(HttpResponseStatus.TEMPORARY_REDIRECT.code())
                .headers().set(HttpHeaders.LOCATION, uri);
        response.end();
    }

    public static String titleWithTotal(final String label, final long total) {
        return String.format(label + " [%d data found]", total);
    }

    public static <T> Handler<AsyncResult<Message<T>>> catchHandler(final ConsumerInterface<AsyncResult<Message<T>>> consumer, final RoutingContext context) {
        return r -> {
            try {
                consumer.accept(r);
            } catch (Exception e) {
                context.fail(e);
            }
        };
    }

    public static Handler<RoutingContext> webHandler(final ConsumerInterface<RoutingContext> consumer) {
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

    public static PaginationTemplateBuilder createPaginationTemplateBuilder(final String uriPath, final Pagination pagination, final int paginationNavLength) {
        int size = pagination.getSize();
        return new PaginationTemplateBuilder()
                .addClass(BootstrapCss.PULL_RIGHT.value)
                .first(uriPath + pageQueryString(pagination.first(), size), pagination.isFirst())
                .prev(uriPath + pageQueryString(pagination.prev(), size), pagination.hasPrev())
                .addAllItems(items -> {
                    pagination.nav(paginationNavLength).forEach(p -> items.add(
                            new PaginationItemTemplateBuilder()
                                    .setLabel("" + p)
                                    .setHref(uriPath + pageQueryString(p, size))
                                    .addClass(pagination.isCurrentPage(p) ? BootstrapCss.ACTIVE.value : "")
                                    .createPaginationItemTemplate()
                    ));
                })
                .next(uriPath + pageQueryString(pagination.next(), size), pagination.hasNext())
                .last(uriPath + pageQueryString(pagination.last(), size), pagination.isLast());
    }

    private static String pageQueryString(int prev, int size) {
        return String.format("?page=%d&size=%d", prev, size);
    }
}
