package io.crm.web.util.parsers;

import com.google.common.collect.ImmutableList;
import io.crm.util.SimpleCounter;
import io.vertx.core.json.JsonObject;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

/**
 * Created by someone on 12/10/2015.
 */
final public class CsvParser {
    final CSVFormat csvFormat = CSVFormat.EXCEL
            .withIgnoreEmptyLines();
    final List<Function<String, Object>> converters;

    public CsvParser(final List<Function<String, Object>> converters) {
        this.converters = converters;
    }

    public final CsvParseResult parse(final File file) {
        return parse(file, converters);
    }

    public final CsvParseResult parse(final File file, final List<Function<String, Object>> localConverters) {
        try {
            final ImmutableList.Builder<JsonObject> errorListBuilder = ImmutableList.builder();
            Reader in = new FileReader(file);

            final CSVParser parser = csvFormat.parse(in);
            final Iterator<CSVRecord> iterator = parser.iterator();
            final List<String> headers = headers(iterator);
            final List<List<Object>> body = body(iterator, localConverters, errorListBuilder);
            return new CsvParseResult(headers, body, errorListBuilder.build());
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<List<Object>> body(final Iterator<CSVRecord> iterator, final List<Function<String, Object>> converters, final ImmutableList.Builder<JsonObject> errors) {
        final ImmutableList.Builder<List<Object>> builder = ImmutableList.builder();

        iterator.forEachRemaining(csvRecord -> {

            if (csvRecord.size() < converters.size()) {
                addErrorForLine(errors, csvRecord);
                return; //Continue
            }

            final SimpleCounter counter = new SimpleCounter(0);
            final ImmutableList.Builder<Object> listBuilder = ImmutableList.builder();
            converters.forEach(converter -> {
                String str = null;
                try {
                    str = csvRecord.get(counter.counter++);
                    final Object val = converter.apply(str);
                    listBuilder.add(val);
                } catch (Exception ex) {
                    final long recordNumber = csvRecord.getRecordNumber();
                    addError(errors, recordNumber, counter, str, ex, converter);
                }
            });
            builder.add(listBuilder.build());
        });
        return builder.build();
    }

    private void addErrorForLine(ImmutableList.Builder<JsonObject> errors, CSVRecord csvRecord) {
        errors.add(
                new CsvParseErrorBuilder()
                        .setLine(csvRecord.getRecordNumber())
                        .setValue(csvRecord.toString())
                        .setMessageCode("field.missing")
                        .setMessage(String.format("Line: %d: Some field is missing.[%s]", csvRecord.getRecordNumber(), csvRecord.toString()))
                        .createCsvParseError()
        );
    }

    private void addError(ImmutableList.Builder<JsonObject> errors, long recordNumber, SimpleCounter counter, String str, final Exception ex, final Function<String, Object> converter) {
        errors.add(
                new CsvParseErrorBuilder()
                        .setLine(recordNumber)
                        .setCol(counter.counter)
                        .setValue(str)
                        .setMessageCode("field.invalid")
                        .setMessage(String.format("Line: %d, Col: %d: Invalid field value.[%s]", recordNumber, counter.counter, str))
                        .setException(ex)
                        .setConverter(converter.getClass().getName())
                        .createCsvParseError()
        );
    }

    private List<String> headers(final Iterator<CSVRecord> iterator) {
        if (iterator.hasNext()) {
            final CSVRecord next = iterator.next();

            final ImmutableList.Builder<String> builder = ImmutableList.builder();
            next.forEach(val -> builder.add(val));
            final ImmutableList<String> list = builder.build();
            String[] headers = list.toArray(new String[list.size()]);
            return ImmutableList.copyOf(headers);
        }
        return ImmutableList.of();
    }
}
