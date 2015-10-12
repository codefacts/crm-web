package io.crm.web.controller;

import io.crm.web.WebApp;
import io.crm.web.WebST;
import io.crm.web.WebUris;
import io.vertx.ext.web.Router;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by someone on 04/10/2015.
 */
public class ImageController {
    public static final String image_directory_prop = "IMAGE_DIRECTORY";
    public static final String IMAGE_DIRECTORY = WebApp.loadConfig().getString(image_directory_prop);

    public ImageController(final Router router) {
        renderImage(router);
    }

    public void renderImage(final Router router) {
        router.get(WebUris.BrCheckerImages.value).handler(ctx -> {
            try {
                final String imageName = ctx.request().params().get(WebST.name).replace(".PNG", "");
                final File dir = new File(IMAGE_DIRECTORY);
                if (!dir.exists()) {
                    try {
                        Files.createDirectories(Paths.get(IMAGE_DIRECTORY));
                    } catch (IOException e) {
                        ctx.fail(e);
                    }
                }
                final String imageFile = dir.listFiles((f, n) -> n.startsWith(imageName))[0].getAbsolutePath();
                ctx.response().sendFile(imageFile);

            } catch (Exception e) {
                ctx.fail(e);
            }

        });
    }
}
