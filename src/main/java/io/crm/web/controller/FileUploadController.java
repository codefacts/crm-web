package io.crm.web.controller;

import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.template.*;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.watertemplate.Template;

import java.util.Optional;

import static io.crm.web.Uris.fileUpload;

/**
 * Created by someone on 01/10/2015.
 */
public class FileUploadController {
    private final Vertx vertx;
    private final String uploadDirectory = "C:\\Users\\skpaul\\Desktop\\New folder";

    public FileUploadController(final Vertx vertx, final Router router) {
        this.vertx = vertx;
        uploadForm(router);
        doUpload(router);
    }

    public void uploadForm(final Router router) {
        router.get(fileUpload.value).handler(ctx -> {
            System.out.println("upload form");
            ctx.response().end(
                    new PageBuilder(fileUpload.label)
                            .body(
                                    new DashboardTemplateBuilder()
                                            .setUser(ctx.session().get(ST.currentUser))
                                            .setSidebarTemplate(
                                                    new SidebarTemplate(ctx.request().uri())
                                            )
                                            .setContentTemplate(
                                                    form()
                                            )
                                            .build()
                            )
                            .build().render()
            );
        });
    }

    public void doUpload(final Router router) {
        router.post(Uris.fileUpload.value).handler(BodyHandler.create());
        router.post(Uris.fileUpload.value)
                .handler(ctx -> {

                    ctx.request().exceptionHandler(ctx::fail);

                    System.out.println("fileUploads: " + ctx.fileUploads().size());
                    final Optional<FileUpload> fileUpload = ctx.fileUploads().stream().findFirst();
                    if (!fileUpload.isPresent()) {
                        ctx.response()
                                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                                .end("No File");
                        return;
                    }
                    final FileUpload upload = fileUpload.get();
                    vertx.fileSystem().exists(filepath(upload.fileName()), r -> {
                        if (r.failed()) {
                            ctx.fail(r.cause());
                            return;
                        }
                        final Boolean exists = r.result();
                        if (exists) {
                            ctx.response().setStatusCode(HttpResponseStatus.BAD_REQUEST.code());
                            ctx.response().end("File already exists.");
                            return;
                        }
                        vertx.fileSystem().copy(upload.uploadedFileName(), filepath(upload.fileName()), r1 -> {
                            if (r1.failed()) {
                                ctx.fail(r1.cause());
                                return;
                            }

                            ctx.response().end("ok");
                        });
                    });
                });
    }

    private String filepath(String fileName) {
        return uploadDirectory + "/" + fileName;
    }

    private Template form() {
        return new FileUploadTemplate();
    }
}
