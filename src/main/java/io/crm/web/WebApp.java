package io.crm.web;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by someone on 07/09/2015.
 */
final public class WebApp {
    private static final JsonObject config = loadConfig();
    private static final String CONFIG_FILE_NAME = "config.json";
    public static final String STATIC_DIRECTORY = WebApp.loadConfig().getString("STATIC_DIRECTORY");
    public static final String PUBLIC_DIRECTORY = WebApp.loadConfig().getString("PUBLIC_DIRECTORY");

    static {
        if (loadConfig().getBoolean("dev-mode")) {
            System.setProperty("dev-mode", "true");
        }
    }

    public static JsonObject loadConfig() {
        try {
            if (config == null) {
                final File file = new File(CONFIG_FILE_NAME);
                if (file.exists()) {
                    return new JsonObject(FileUtils.readFileToString(file));
                }
                final InputStream inputStream = WebApp.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
                return new JsonObject(IOUtils.toString(inputStream));
            } else return config;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) {
        Vertx.vertx().deployVerticle(new WebMainVerticle());
    }
}
