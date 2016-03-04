package io.crm.web.util.parsers;

import io.vertx.core.json.JsonArray;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import rx.Observable;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by shahadat on 3/4/16.
 */
public class CsvFileParser {
    final CSVFormat csvFormat;

    public CsvFileParser(CSVFormat csvFormat) {
        this.csvFormat = csvFormat;
    }

    public CsvFileParser() {
        this(CSVFormat.EXCEL
            .withIgnoreEmptyLines());
    }

    public Observable<JsonArray> parse(File file) {
        try {
            CSVParser parser = csvFormat.parse(new FileReader(file));
            return Observable.from(parser)
                .map(r -> {

                    String[] array = new String[r.size()];
                    for (int i = 0; i < r.size(); i++) {
                        array[i] = r.get(i);
                    }

                    return new JsonArray(Arrays.asList(array));
                });
        } catch (IOException e) {
            e.printStackTrace();
            return Observable.error(e);
        }
    }

    public static void main(String[] args) {
        CsvFileParser parser = new CsvFileParser();
        parser.parse(new File("/home/shahadat/Downloads", "export.csv"));
    }
}
