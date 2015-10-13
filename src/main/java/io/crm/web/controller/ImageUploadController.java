package io.crm.web.controller;

import com.google.common.collect.ImmutableList;
import io.crm.util.TaskCoordinator;
import io.crm.util.TaskCoordinatorBuilder;
import io.crm.web.WebApp;
import io.crm.web.WebST;
import io.crm.web.WebUris;
import io.crm.web.template.*;
import io.crm.web.util.WebUtils;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static io.crm.web.util.WebUtils.catchHandler;
import static io.crm.web.util.WebUtils.webHandler;

/**
 * Created by someone on 13/10/2015.
 */
final public class ImageUploadController {
    private static final String FLASH_DO_UPLOAD = "IMAGE_UPLOAD_CONTROLLER.FLASH.DO_UPLOAD";
    private final Vertx vertx;

    public ImageUploadController(final Vertx vertx, final Router router) {
        this.vertx = vertx;
        uploadForm(router);
        doUpload(router);
    }

    public void uploadForm(final Router router) {
        router.get(WebUris.imageUpload.value).handler(webHandler(
                ctx -> {
                    ctx.response().end(
                            new PageBuilder(WebUris.imageUpload.label)
                                    .body(
                                            new DashboardTemplateBuilder()
                                                    .setSidebarTemplate(
                                                            new SidebarTemplateBuilder()
                                                                    .setCurrentUri(ctx.request().uri())
                                                                    .createSidebarTemplate()
                                                    )
                                                    .setContentTemplate(
                                                            new ImageUploadForm(ctx.session().get(FLASH_DO_UPLOAD))
                                                    )
                                                    .setUser(ctx.session().get(WebST.currentUser))
                                                    .build()
                                    )
                                    .build().render()
                    );
                    ctx.session().remove(FLASH_DO_UPLOAD);
                }));
    }

    public void doUpload(final Router router) {
        router.post(WebUris.imageUpload.value).handler(BodyHandler.create());
        router.post(WebUris.imageUpload.value).handler(webHandler(
                ctx -> {
                    ctx.request().exceptionHandler(ctx::fail);
                    final ImmutableList.Builder<UploadResult> builder = ImmutableList.builder();

                    final TaskCoordinator taskCoordinator = new TaskCoordinatorBuilder()
                            .count(ctx.fileUploads().size())
                            .onError(ctx::fail)
                            .onSuccess(() -> {
                                ctx.session().put(FLASH_DO_UPLOAD, builder.build());
                                ctx.response().end(new JavascriptRedirect(WebUris.imageUpload.value).render());
                            })
                            .get();
                    ctx.fileUploads().forEach(fu -> {
                        vertx.fileSystem()
                                .copy(fu.uploadedFileName(),
                                        Paths.get(ImageController.IMAGE_DIRECTORY, fu.fileName()).toString(),
                                        catchHandler(r -> {
                                            if (r.failed()) {
                                                builder.add(
                                                        new UploadResultBuilder()
                                                                .setFilename(fu.fileName())
                                                                .setStatus(Status.error)
                                                                .setMessage(
                                                                        r.cause().getCause() instanceof FileAlreadyExistsException
                                                                                ? "File already exists." : r.getClass().getSimpleName() + ": " + r.cause().getMessage()
                                                                )
                                                                .createUploadResult()
                                                );
                                                taskCoordinator.countdown();
                                                return;
                                            }
                                            builder.add(
                                                    new UploadResultBuilder()
                                                            .setFilename(fu.fileName())
                                                            .setStatus(Status.success)
                                                            .setMessage("File upload successfull.")
                                                            .createUploadResult()
                                            );
                                            taskCoordinator.countdown();
                                        }, ctx));
                    });
                }));
    }

    public static class UploadResult {
        private final Status status;
        private final String message;
        private final String filename;

        public UploadResult(Status status, String message, String filename) {
            this.status = status;
            this.message = message;
            this.filename = filename;
        }

        public Status getStatus() {
            return status;
        }

        public String getMessage() {
            return message;
        }

        public String getFilename() {
            return filename;
        }
    }

    public enum Status {
        error, success
    }
}
