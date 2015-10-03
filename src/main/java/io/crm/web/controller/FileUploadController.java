package io.crm.web.controller;

import io.crm.FailureCode;
import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.css.bootstrap.TableClasses;
import io.crm.web.template.*;
import io.crm.web.template.table.*;
import io.crm.web.util.WebUtils;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.watertemplate.Template;

import java.io.File;
import java.util.Optional;

import static io.crm.web.ApiEvents.UPLOAD_BR_CHECKER_DATA;
import static io.crm.web.Uris.fileUpload;
import static io.crm.web.util.WebUtils.webHandler;

/**
 * Created by someone on 01/10/2015.
 */
public class FileUploadController {
    private static final String FLASH_DO_UPLOAD = "FILE_UPLOAD_CONTROLLER.FLASH.DO_UPLOAD";
    private final Vertx vertx;

    public FileUploadController(final Vertx vertx, final Router router) {
        this.vertx = vertx;
        uploadForm(router);
        doUpload(router);
    }

    public void uploadForm(final Router router) {
        router.get(fileUpload.value).handler(webHandler(ctx -> {
            System.out.println("upload form");
            ctx.response().end(
                    new PageBuilder(fileUpload.label)
                            .body(
                                    new DashboardTemplateBuilder()
                                            .setUser(ctx.session().get(ST.currentUser))
                                            .setSidebarTemplate(
                                                    new SidebarTemplateBuilder()
                                                            .setCurrentUri(ctx.request().uri())
                                                            .createSidebarTemplate()
                                            )
                                            .setContentTemplate(
                                                    form(ctx)
                                            )
                                            .build()
                            )
                            .build().render()
            );
        }));
    }

    public void doUpload(final Router router) {
        router.post(Uris.fileUpload.value).handler(BodyHandler.create());
        router.post(Uris.fileUpload.value)
                .handler(webHandler(ctx -> {

                    ctx.request().exceptionHandler(ctx::fail);

                    final Optional<FileUpload> fileUpload = ctx.fileUploads().stream().findFirst();
                    FileUpload upload = fileUpload.get();
                    if (!fileUpload.isPresent() || upload.size() <= 0 || upload.name().isEmpty()) {
                        ctx.session().put(FLASH_DO_UPLOAD,
                                new JsonObject()
                                        .put(ST.statusCode, StatusCode.fileMissing.name()));
                        ctx.response()
                                .setStatusCode(HttpResponseStatus.BAD_REQUEST.code())
                                .end(new JavascriptRedirect(Uris.fileUpload.value).render());
                        return;
                    }
                    vertx.eventBus().send(UPLOAD_BR_CHECKER_DATA, new File(upload.uploadedFileName()).getAbsolutePath(), (AsyncResult<Message<Integer>> r) -> {
                        if (r.failed()) {
                            if (!(r.cause() instanceof ReplyException)) {
                                ctx.fail(r.cause());
                                return;
                            }
                            ReplyException ex = (ReplyException) r.cause();
                            if (!(ex.failureCode() == FailureCode.BadRequest.code)) {
                                ctx.fail(r.cause());
                                return;
                            }
                            ctx.session().put(FLASH_DO_UPLOAD,
                                    new JsonObject()
                                            .put(ST.statusCode, StatusCode.error.name())
                                            .put(ST.body, new JsonObject(ex.getMessage())));
                            ctx.response().end(new JavascriptRedirect(Uris.fileUpload.value).render());
                            return;
                        }
                        ctx.session().put(FLASH_DO_UPLOAD,
                                new JsonObject()
                                        .put(ST.statusCode, StatusCode.success.name())
                                        .put(ST.body, r.result().body()));
                        ctx.response().end(new JavascriptRedirect(Uris.fileUpload.value).render());
                    });
                }));
    }

    private String renderUploadError(final JsonObject entries) {

        return new TableTemplateBuilder()
                .addClass(TableClasses.STRIPED.value)
                .setHeader(
                        new TableHeaderBuilder()
                                .addTableRows(rows -> {
                                    rows.add(
                                            new TableRowBuilder()
                                                    .addTableCells(cells -> {
                                                        cells.add(
                                                                new ThBuilder()
                                                                        .setBody("Line Number")
                                                                        .createTh()
                                                        );
                                                        cells.add(
                                                                new ThBuilder()
                                                                        .setBody("Error Details")
                                                                        .createTh()
                                                        );
                                                    })
                                                    .createTableRow()
                                    );
                                })
                                .createTableHeader()
                )
                .setBody(
                        new TableBodyBuilder()
                                .addTableRows(rows -> {
                                    entries.getMap().forEach((k, v) -> {
                                        rows.add(
                                                new TableRowBuilder()
                                                        .addTableCells(cells -> {
                                                            cells.add(
                                                                    new TableCellBuilder()
                                                                            .setBody(k)
                                                                            .createTableCell()
                                                            );
                                                            cells.add(
                                                                    new TableCellBuilder()
                                                                            .setBody(v == null ? "" : v.toString())
                                                                            .createTableCell()
                                                            );
                                                        })
                                                        .createTableRow()
                                        );
                                    });
                                })
                                .createTableBody()
                )
                .createTableTemplate().render();
    }

    private String renderUploadSuccess(final int insertCount) {
        return
                new AlertTemplateBuilder()
                        .success(String.format("%d data uploaded successfully.", insertCount))
                        .createAlertTemplate().render();
    }

    private Template form(final RoutingContext ctx) {
        Object o = ctx.session().get(FLASH_DO_UPLOAD);
        ctx.session().remove(FLASH_DO_UPLOAD);

        if (o != null && o instanceof JsonObject) {
            if (((JsonObject) o).getString(ST.statusCode, "").equals(StatusCode.success.name())) {
                return new FileUploadTemplate(
                        renderUploadSuccess(((JsonObject) o).getInteger(ST.body))
                );
            } else if (((JsonObject) o).getString(ST.statusCode, "").equals(StatusCode.error.name())) {
                return new FileUploadTemplate(
                        renderUploadError(((JsonObject) o).getJsonObject(ST.body)));
            } else if (((JsonObject) o).getString(ST.statusCode, "").equals(StatusCode.fileMissing.name())) {
                return new FileUploadTemplate(
                        new AlertTemplateBuilder()
                                .danger("No file is uploaded.")
                                .createAlertTemplate().render());
            }
        }
        return new FileUploadTemplate("");
    }

    private enum StatusCode {
        success, error, fileMissing
    }
}
