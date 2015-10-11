package io.crm.web;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.FileUtils;

import java.io.*;

import static org.apache.commons.io.FileUtils.readFileToString;

/**
 * Created by someone on 07/09/2015.
 */
public class App {
    private static final JsonObject config = loadConfig();
    private static final String CONFIG_FILE_NAME = "config.json";
    public static final String STATIC_DIRECTORY = App.loadConfig().getString("STATIC_DIRECTORY");
    public static final String PUBLIC_DIRECTORY = App.loadConfig().getString("PUBLIC_DIRECTORY");

    public static JsonObject loadConfig() {
        if (config == null) {
            final File file = new File(CONFIG_FILE_NAME);
            if (file.exists()) {
                try {
                    return new JsonObject(FileUtils.readFileToString(file));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            final InputStream inputStream = App.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
            final InputStreamReader reader = new InputStreamReader(inputStream);
            final StringBuilder builder = new StringBuilder();
            final char[] chars = new char[1024];
            int read;
            try {
                while ((read = reader.read(chars, 0, chars.length)) > 0) {
                    builder.append(chars, 0, read);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new JsonObject(builder.toString());
        } else return config;
    }

//    static {
//        System.setProperty("dev-mode", "true");
//    }

    public static void main(String... args) {
        Vertx.vertx().deployVerticle(new MainVerticle());
    }
}
