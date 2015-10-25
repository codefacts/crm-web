package io.crm.web.template.page.renderer;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

public class BrDetailsRendererBuilder {
    private RoutingContext ctx;
    private String title;
    private JsonObject pagination;
    private List<JsonObject> data;
    private List<String> callStatuses;

    public BrDetailsRendererBuilder setCtx(RoutingContext ctx) {
        this.ctx = ctx;
        return this;
    }

    public BrDetailsRendererBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public BrDetailsRendererBuilder setPagination(JsonObject pagination) {
        this.pagination = pagination;
        return this;
    }

    public BrDetailsRendererBuilder setData(List<JsonObject> data) {
        this.data = data;
        return this;
    }

    public BrDetailsRenderer createBrDetailsRenderer() {
        return new BrDetailsRenderer(ctx, callStatuses, title, pagination, data);
    }

    public BrDetailsRendererBuilder setCallStatuses(final List<String> list) {
        this.callStatuses = list;
        return this;
    }
}