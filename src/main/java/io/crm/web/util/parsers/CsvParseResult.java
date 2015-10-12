package io.crm.web.util.parsers;

import io.vertx.core.json.JsonObject;

import java.util.List;

/**
 * Created by someone on 12/10/2015.
 */
final public class CsvParseResult {
    public final List<String> headers;
    public final List<List<Object>> body;
    public final List<JsonObject> errors;

    public CsvParseResult(final List<String> headers, final List<List<Object>> body, List<JsonObject> errors) {
        this.headers = headers;
        this.body = body;
        this.errors = errors;
    }

    public boolean hasErrors() {
        return errors != null && errors.size() > 0;
    }
}
