package io.crm.web.controller;

import com.google.common.collect.ImmutableList;
import io.crm.util.TaskCoordinator;
import io.crm.util.TaskCoordinatorBuilder;
import io.crm.web.MessageCodes;
import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.template.*;
import io.crm.web.util.Status;
import io.crm.web.util.UploadResult;
import io.vertx.core.Vertx;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

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
        router.get(Uris.imageUpload.value).handler(webHandler(
                ctx -> {
                    ctx.response().end(
                            new PageBuilder(Uris.imageUpload.label)
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
                                                    .setUser(ctx.session().get(ST.currentUser))
                                                    .build()
                                    )
                                    .build().render()
                    );
                    ctx.session().remove(FLASH_DO_UPLOAD);
                }));
    }

    public void doUpload(final Router router) {
        router.post(Uris.imageUpload.value).handler(BodyHandler.create());
        router.post(Uris.imageUpload.value).handler(webHandler(
                ctx -> {
                    ctx.request().exceptionHandler(ctx::fail);
                    final ImmutableList.Builder<UploadResult> builder = ImmutableList.builder();


                    final Iterator<FileUpload> iterator = ctx.fileUploads().iterator();
                    while (iterator.hasNext()) {
                        final String fileName = iterator.next().fileName();
                        if (!validImageName(fileName)) {
                            builder.add(
                                    new UploadResultBuilder()
                                            .setStatus(Status.error)
                                            .setFilename(fileName)
                                            .setMessageCode(MessageCodes.filename_invalid)
                                            .createUploadResult()
                            );
                            iterator.remove();
                        }
                    }


                    final TaskCoordinator taskCoordinator = new TaskCoordinatorBuilder()
                            .count(ctx.fileUploads().size())
                            .onError(ctx::fail)
                            .onSuccess(() -> {
                                ctx.session().put(FLASH_DO_UPLOAD, builder.build());
                                ctx.response().end(new JavascriptRedirect(Uris.imageUpload.value).render());
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
                                                                .setMessageCode(
                                                                        r.cause().getCause() instanceof FileAlreadyExistsException
                                                                                ? MessageCodes.file_already_exists
                                                                                : MessageCodes.unknownError
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
                                                            .setMessageCode(MessageCodes.fileuploadSuccess)
                                                            .createUploadResult()
                                            );
                                            taskCoordinator.countdown();
                                        }, ctx));
                    });
                }));
    }

    private static boolean validImageName(String imageName) {
        final String[] split = imageName.split("!");
        if (split.length < 5) return false;
        final boolean matches = split[0].matches("\\d{1,10}");
        final boolean matches1 = split[2].matches("\\d{1,10}");
        final boolean matches2 = split[3].matches("\\d{2}-[a-zA-Z]{3}-\\d{4}");
        final int indexOf = imageName.lastIndexOf('.');
        final boolean png = indexOf > 0
                && (indexOf + 1) < imageName.length()
                ? imageName.substring(indexOf + 1).equalsIgnoreCase("png") : false;
        return matches & matches1 & matches2 & png;
    }

    public static void main(String[] args) throws java.lang.Exception {
        System.out.println(new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a").format(new Date()));
        String imageName = "58!stongi!1!04-Oasdfct-2015!-688539836 - Copy - Copy (2).png";

        System.out.println(validImageName(imageName));
        return;
    }
}
