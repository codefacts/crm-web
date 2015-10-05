package io.crm.web.template;

import io.crm.util.Touple1;
import io.crm.util.Util;
import io.crm.web.css.bootstrap.TableClasses;
import io.crm.web.service.callreview.model.BrCheckerModel;
import io.crm.web.template.table.*;
import io.vertx.core.json.JsonObject;
import org.watertemplate.Template;

/**
 * Created by someone on 04/10/2015.
 */
public class BrCheckerInfoView extends Template {
    public BrCheckerInfoView(final JsonObject info) {

        final String img = String.format("/br-checker/images?name=%s", info.getString(BrCheckerModel.PICTURE_NAME.name()));

        add("img", img);
        add("lat", info.getDouble(BrCheckerModel.Latitude.name(), 0.0) + "");
        add("lng", info.getDouble(BrCheckerModel.Longitude.name(), 0.0) + "");

        System.out.println(img);

        add("table", new TableTemplateBuilder()
                .addClass(TableClasses.STRIPED.value)
                .setBody(
                        new TableBodyBuilder()
                                .addTableRows(rows -> {
                                    info.getMap().forEach((k, v) -> {

                                        BrCheckerModel model = null;
                                        try {
                                            model = BrCheckerModel.valueOf(k);
                                        } catch (IllegalArgumentException ex) {
                                            return;
                                        }
                                        if (!model.visible) return;

                                        String val = "";
                                        if (k.equals(BrCheckerModel.BAND.name())) {
                                            val = ((Integer) v) > 0 ? "Yes" : "No";
                                        } else if (k.equals(BrCheckerModel.CONTACTED.name())) {
                                            val = ((Integer) v) > 0 ? "Yes" : "No";
                                        } else if (k.equals(BrCheckerModel.NAME_MATHCH.name())) {
                                            val = ((Integer) v) > 0 ? "Yes" : "No";
                                        } else {
                                            val = v == null ? "" : v + "";
                                        }

                                        final Touple1<String> stringTouple1 = new Touple1<>();
                                        final Touple1<BrCheckerModel> touple1 = new Touple1<>();
                                        touple1.t1 = model;
                                        stringTouple1.t1 = val;
                                        rows.add(
                                                new TableRowBuilder()
                                                        .addTableCells(cells -> {
                                                            cells.add(
                                                                    new ThBuilder()
                                                                            .setBody(touple1.t1 == null ? "" : touple1.t1.label)
                                                                            .createTh()
                                                            );
                                                            cells.add(
                                                                    new TableCellBuilder()
                                                                            .setBody(
                                                                                    stringTouple1.t1 == null ? "" : stringTouple1.t1
                                                                            )
                                                                            .createTableCell()
                                                            );
                                                        })
                                                        .createTableRow()
                                        );
                                    });
                                })
                                .createTableBody()
                )
                .createTableTemplate().render());
    }

    @Override
    protected String getFilePath() {
        return Page.templatePath("br-checker-info-view.html");
    }
}
