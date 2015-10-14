package io.crm.web.controller;

import com.google.common.collect.ImmutableList;
import io.crm.FailureCode;
import io.crm.util.*;
import io.crm.web.ApiEvents;
import io.crm.web.ST;
import io.crm.web.Uris;
import io.crm.web.css.bootstrap.TableClasses;
import io.crm.web.service.callreview.BrCheckerDetailsService;
import io.crm.web.template.*;
import io.crm.web.template.bootstrap.BodyPanelDefaultBuilder;
import io.crm.web.template.table.*;
import io.crm.web.util.Converters;
import io.crm.web.util.DateConverter;
import io.crm.web.util.parsers.CsvParseError;
import io.crm.web.util.parsers.CsvParseResult;
import io.crm.web.util.parsers.CsvParser;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.watertemplate.Template;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.crm.web.ApiEvents.UPLOAD_BR_CHECKER_DATA;
import static io.crm.web.Uris.fileUpload;
import static io.crm.web.util.WebUtils.webHandler;

/**
 * Created by someone on 01/10/2015.
 */
public class FileUploadController {
    private static final String FLASH_DO_UPLOAD = "FILE_UPLOAD_CONTROLLER.FLASH.DO_UPLOAD";
    private final Vertx vertx;
    private final CsvParser csvParser;

    {
        final DateConverter dateConverter = new DateConverter(BrCheckerDetailsService.DATE_FORMAT_STR);

        csvParser = new CsvParser(ImmutableList.of(

                s -> s,

                Converters::toInt,
                Converters::toInt,

                s -> s,
                s -> s,
                s -> s,

                Converters::yesNoToBoolean,
                Converters::yesNoToBoolean,
                Converters::yesNoToBoolean,

                s -> s,

                s -> s,

                s -> s,

                s -> s,
                s -> s,
                s -> s,

                s -> s,
                s -> s,
                Converters::toInt,

                s -> s,
                s -> s,
                s -> s,

                Converters::toDouble,
                Converters::toDouble,
                Converters::toDouble
        ));
    }

    public FileUploadController(final Vertx vertx, final Router router) {
        this.vertx = vertx;
        uploadForm(router);
        doUpload(router);
    }

    public void doUpload(final Router router) {
        router.post(Uris.fileUpload.value).handler(BodyHandler.create());
        router.post(Uris.fileUpload.value)
                .handler(webHandler(ctx -> {

                    ctx.request().exceptionHandler(ctx::fail);

                    final ImmutableList.Builder<Touple2<String, JsonObject>> success = ImmutableList.builder();
                    final ImmutableList.Builder<Touple2<String, Throwable>> errors = ImmutableList.builder();

                    final TaskCoordinator taskCoordinator = new TaskCoordinatorBuilder()
                            .onError(ctx::fail)
                            .onSuccess(() -> {
                                ctx.session().put(FLASH_DO_UPLOAD,
                                        new JsonObject()
                                                .put("success", success.build())
                                                .put("error", errors.build()));
                                ctx.response().end(new JavascriptRedirect(Uris.fileUpload.value).render());
                            })
                            .count(ctx.fileUploads().size())
                            .get();

                    ctx.fileUploads().forEach(fu -> {

                        try {
                            if (fu == null || fu.size() <= 0) {

                                success.add(new Touple2<String, JsonObject>(fu.fileName(),
                                        new JsonObject()
                                                .put(ST.statusCode, StatusCode.fileMissing.name())));
                                taskCoordinator.countdown();
                                return;
                            }

                            handleFileUpload(fu, r -> {
                                try {
                                    if (r.failed()) {
                                        if (r.cause() instanceof HandlerException) {
                                            success.add(new Touple2<String, JsonObject>(fu.fileName(),
                                                    ((HandlerException) r.cause()).value));
                                        } else {
                                            errors.add(new Touple2(fu.fileName(), r.cause()));
                                        }
                                        taskCoordinator.countdown();
                                        return;
                                    }
                                    taskCoordinator.countdown();
                                    success.add(new Touple2<String, JsonObject>(fu.fileName(),
                                            r.result()));
                                } catch (Exception ex) {
                                    ctx.fail(ex);
                                }
                            });
                        } catch (Exception ex) {
                            taskCoordinator.countdown();
                            errors.add(new Touple2<String, Throwable>(fu.fileName(), ex));
                        }

                    });

                }));
    }

    private void handleFileUpload(FileUpload upload, Handler<AsyncResult<JsonObject>> handler) {

        final String file = upload.uploadedFileName();
        final String extention = upload.fileName().substring(
                upload.fileName().lastIndexOf('.') + 1
        );

        if (extention.equalsIgnoreCase("CSV")) {
            parseCsvAndSendData(file, handler);
        } else {
            sendExcelFile(file, extention, handler);
        }
    }

    public void uploadForm(final Router router) {
        router.get(fileUpload.value).handler(webHandler(ctx -> {
            System.out.println("upload form");
            vertx.executeBlocking(
                    f -> {
                        try {
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
                                                                    _multiform(ctx)
                                                            )
                                                            .build()
                                            )
                                            .build().render()
                            );
                            f.complete();
                        } catch (Exception ex) {
                            f.fail(ex);
                        }
                    },
                    r -> {
                        if (r.failed()) {
                            ctx.fail(r.cause());
                        }
                    });
        }));
    }

    private void parseCsvAndSendData(final String file, final Handler<AsyncResult<JsonObject>> handler) {
        final CsvParseResult parseResult = csvParser.parse(new File(file));
        if (parseResult.hasErrors()) {
            SimpleCounter c = new SimpleCounter();
            handler.handle(AsyncUtil.fail(new HandlerException(
                    new JsonObject()
                            .put(ST.statusCode, StatusCode.error.name())
                            .put(ST.body, new JsonObject(
                                    parseResult.errors
                                            .stream()
                                            .collect(Collectors.toMap(
                                                    v -> c.counter++ + "",
                                                    k -> k.getString(CsvParseError.message)))
                            ))
            )));
            return;
        }
        vertx.eventBus().send(ApiEvents.INSERT_BR_CHECKER_INFO,
                new JsonObject()
                        .put(ST.headers, parseResult.headers)
                        .put(ST.body, parseResult.body),
                r -> {
                    try {
                        if (r.failed()) {
                            handler.handle(AsyncUtil.fail(r.cause()));
                            return;
                        }

                        handler.handle(AsyncUtil.success(
                                new JsonObject()
                                        .put(ST.statusCode, StatusCode.success.name())
                                        .put(ST.body, r.result().body())
                        ));

                    } catch (Exception ex) {
                        handler.handle(AsyncUtil.fail(ex));
                    }
                });
    }

    private void sendExcelFile(final String file, final String extention, final Handler<AsyncResult<JsonObject>> handler) {
        vertx.eventBus().send(UPLOAD_BR_CHECKER_DATA,
                new JsonObject()
                        .put(ST.file, new File(file).getAbsolutePath())
                        .put(ST.extention, extention),
                new DeliveryOptions()
                        .setSendTimeout(30 * 60 * 1000),
                (AsyncResult<Message<Integer>> r) -> {

                    if (r.failed()) {
                        if (!(r.cause() instanceof ReplyException)) {
                            handler.handle(AsyncUtil.fail(r.cause()));
                            return;
                        }
                        ReplyException ex = (ReplyException) r.cause();
                        if (!(ex.failureCode() == FailureCode.BadRequest.code)) {
                            handler.handle(AsyncUtil.fail(r.cause()));
                            return;
                        }
                        handler.handle(AsyncUtil.success(
                                new JsonObject()
                                        .put(ST.statusCode, StatusCode.error.name())
                                        .put(ST.body, new JsonObject(ex.getMessage()))
                        ));
                        return;
                    }

                    if (r.result().body() <= 0) {

                        handler.handle(AsyncUtil.success(
                                new JsonObject()
                                        .put(ST.statusCode, StatusCode.invalidFile.name())
                                        .put(ST.body, "Invalid file.")
                        ));

                        return;
                    }
                    handler.handle(AsyncUtil.success(
                            new JsonObject()
                                    .put(ST.statusCode, StatusCode.success.name())
                                    .put(ST.body, r.result().body())
                    ));
                });
    }

    public static class HandlerException extends Exception {
        public final JsonObject value;

        public HandlerException(JsonObject value) {
            this.value = value;
        }
    }

    private Template renderUploadError(final JsonObject body) {

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
                                    body.getMap().forEach((k, v) -> {
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
                .createTableTemplate();
    }

    private Template renderUploadSuccess(final int insertCount) {
        return
                new AlertTemplateBuilder()
                        .success(String.format("%d data uploaded successfully.", insertCount))
                        .createAlertTemplate();
    }

    private Template _multiform(RoutingContext ctx) {
        Object o = ctx.session().get(FLASH_DO_UPLOAD);
        ctx.session().remove(FLASH_DO_UPLOAD);

        final ImmutableList.Builder<String> builder = ImmutableList.builder();
        if (o != null && o instanceof JsonObject) {
            final List<Touple2<String, Throwable>> errors = ((JsonObject) o).getJsonArray("error").getList();
            final List<Touple2<String, JsonObject>> success = ((JsonObject) o).getJsonArray("success").getList();

            errors.forEach(t2 -> {
                builder.add(
                        new AlertTemplateBuilder()
                                .info("Error: filename: " + t2.getT1() + ". Error uploading this file.")
                                .createAlertTemplate().render()
                );
            });

            success.forEach(t2 -> {
                builder.add(
                        "<h4>FileName: " + t2.t1 + "</h4>" + form(t2.t2).render()
                );
            });
        }

        return new FileUploadTemplate(String.join("", builder.build()));
    }

    private Template form(final JsonObject o) {

        final String statusCode = ((JsonObject) o).getString(ST.statusCode, "");
        if (statusCode.equals(StatusCode.success.name())) {
            return renderUploadSuccess(((JsonObject) o).getInteger(ST.body));
        } else if (statusCode.equals(StatusCode.invalidFile.name())) {
            return new AlertTemplateBuilder()
                    .info("Invalid File")
                    .createAlertTemplate();
        } else if (statusCode.equals(StatusCode.error.name())) {
            return renderUploadError(((JsonObject) o).getJsonObject(ST.body));
        } else if (statusCode.equals(StatusCode.fileMissing.name())) {
            return new AlertTemplateBuilder()
                    .danger("No file is uploaded.")
                    .createAlertTemplate();
        }

        return new EmptyTemplate();
    }

    private enum StatusCode {
        success, parseError, error, fileMissing, invalidFile;
    }
}
