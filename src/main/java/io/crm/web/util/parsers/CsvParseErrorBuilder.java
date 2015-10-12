package io.crm.web.util.parsers;

import io.crm.util.Util;
import io.vertx.core.json.JsonObject;

import java.util.Iterator;

import static io.crm.util.Util.toJsonRecursive;

public class CsvParseErrorBuilder {
    private long line;
    private int col;
    private String value;
    private Exception exception;
    private String messageCode;
    private String message;
    private String converter;

    public CsvParseErrorBuilder setLine(long line) {
        this.line = line;
        return this;
    }

    public CsvParseErrorBuilder setCol(int col) {
        this.col = col;
        return this;
    }

    public CsvParseErrorBuilder setValue(String value) {
        this.value = value;
        return this;
    }

    public CsvParseErrorBuilder setException(Exception exception) {
        this.exception = exception;
        return this;
    }

    public CsvParseErrorBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public CsvParseErrorBuilder setMessageCode(String messageCode) {
        this.messageCode = messageCode;
        return this;
    }

    public CsvParseErrorBuilder setConverter(String converter) {
        this.converter = converter;
        return this;
    }

    public JsonObject createCsvParseError() {

        final JsonObject jsonObject = new JsonObject()
                .put(CsvParseError.line, line)
                .put(CsvParseError.col, col)
                .put(CsvParseError.value, value)
                .put(CsvParseError.exception, toJsonRecursive(exception))
                .put(CsvParseError.messageCode, messageCode)
                .put(CsvParseError.message, message)
                .put(CsvParseError.converter, converter);
        final Iterator<Object> iterator = jsonObject.getMap().values().iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == null) iterator.remove();
        }
        return jsonObject;
    }
}