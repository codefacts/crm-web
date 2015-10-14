package io.crm.web.controller;

import io.crm.web.App;
import io.crm.web.ST;
import io.crm.web.Uris;
import io.netty.handler.codec.http.HttpResponseStatus;
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
    public static final String IMAGE_DIRECTORY = App.loadConfig().getString(image_directory_prop);

    public ImageController(final Router router) {
        renderImage(router);
    }

    public void renderImage(final Router router) {
        router.get(Uris.BrCheckerImages.value).handler(ctx -> {
            try {
                final String imageName = ctx.request().params().get(ST.name).replace(".PNG", "");
                final File dir = new File(IMAGE_DIRECTORY);
                if (!dir.exists()) {
                    try {
                        Files.createDirectories(Paths.get(IMAGE_DIRECTORY));
                    } catch (IOException e) {
                        ctx.fail(e);
                    }
                }

                final File[] files = dir.listFiles((f, n) -> n.startsWith(imageName));
                if (files.length <= 0) {
                    ctx.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code());
                    ctx.response().end("Image file not found.");
                    return;
                }
                final String imageFile = files[0].getAbsolutePath();
                ctx.response().sendFile(imageFile);

            } catch (Exception e) {
                ctx.fail(e);
            }

        });
    }
}
