package io.crm.web.service.callreview.model;

/**
 * Created by someone on 04/10/2015.
 */
public enum BrCheckerModel {
    id("ID", true),
    CLUSTER_NAME("Cluster Name", true),
    TSR_CODE("TSR Code", true),
    TSR_NAME("TSR Name", true),
    CALL_NO("Call No", true),

    CONSUMER_NAME("Consumer Name", true),
    CONSUMER_MOBILE_NUMBER("Consumer Mobile Number", true),
    CALL_STATUS("Call Status", true),

    BAND("Brand", true),
    CONTACTED("Contacted", true),
    NAME_MATCH("Name Match", true),

    PICTURE_NAME("Picture Name", false),
    TIME_OF_CHECK("Time Of Check", true),

    AUDITOR_CODE("Auditor Code", true),
    AUDITOR_NAME("Auditor Name", true),


    DATE_AND_TIME("Date And Time", true),
    REMARK("Remark", true),

    TOTAL_VISITED("Total Visited", true),
    TRACKER_TABLE_ID("Tracker Table ID", true),

    Accuracy("Accuracy", false),
    Latitude("Latitude", false),
    Longitude("Longitude", false),

    PICTURE_URL("Picture URL", false);

    public final String label;
    public final boolean visible;

    BrCheckerModel(String label, boolean visible) {
        this.label = label;
        this.visible = visible;
    }
}
