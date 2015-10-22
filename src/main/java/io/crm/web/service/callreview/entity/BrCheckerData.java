package io.crm.web.service.callreview.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "br_checker_entrys")
public class BrCheckerData {

    @Id
    private Long id;
    private int TRACKER_TABLE_ID;

    private int CALL_NO;

    private String CONSUMER_NAME;

    private String CONSUMER_MOBILE_NUMBER;

    private boolean NAME_MATCH;

    private String CALL_STATUS;

    private boolean CONTACTED;

    private boolean BAND;

    private String TIME_OF_CHECK;

    @Temporal(TemporalType.TIMESTAMP)
    private Date DATE_AND_TIME;

    private String AUDITOR_NAME;

    private String AUDITOR_CODE;

    private String CLUSTER_NAME;

    private String TSR_CODE;

    private String TSR_NAME;

    private int TOTAL_VISITED;

    private String REMARK;

    private String PICTURE_NAME;

    private String PICTURE_URL;

    private float Latitude;

    private float Longitude;

    private float Accuracy;

    private String ORIGINAL_DATE_STRING;

    public int getTRACKER_TABLE_ID() {
        return TRACKER_TABLE_ID;
    }

    public void setTRACKER_TABLE_ID(int TRACKER_TABLE_ID) {
        this.TRACKER_TABLE_ID = TRACKER_TABLE_ID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getCALL_NO() {
        return CALL_NO;
    }

    public void setCALL_NO(int CALL_NO) {
        this.CALL_NO = CALL_NO;
    }

    public String getCONSUMER_NAME() {
        return CONSUMER_NAME;
    }

    public void setCONSUMER_NAME(String CONSUMER_NAME) {
        this.CONSUMER_NAME = CONSUMER_NAME;
    }

    public String getCONSUMER_MOBILE_NUMBER() {
        return CONSUMER_MOBILE_NUMBER;
    }

    public void setCONSUMER_MOBILE_NUMBER(String CONSUMER_MOBILE_NUMBER) {
        this.CONSUMER_MOBILE_NUMBER = CONSUMER_MOBILE_NUMBER;
    }

    public boolean isNAME_MATCH() {
        return NAME_MATCH;
    }

    public void setNAME_MATCH(boolean NAME_MATCH) {
        this.NAME_MATCH = NAME_MATCH;
    }

    public String getCALL_STATUS() {
        return CALL_STATUS;
    }

    public void setCALL_STATUS(String CALL_STATUS) {
        this.CALL_STATUS = CALL_STATUS;
    }

    public boolean isCONTACTED() {
        return CONTACTED;
    }

    public void setCONTACTED(boolean CONTACTED) {
        this.CONTACTED = CONTACTED;
    }

    public boolean isBAND() {
        return BAND;
    }

    public void setBAND(boolean BAND) {
        this.BAND = BAND;
    }

    public String getTIME_OF_CHECK() {
        return TIME_OF_CHECK;
    }

    public void setTIME_OF_CHECK(String TIME_OF_CHECK) {
        this.TIME_OF_CHECK = TIME_OF_CHECK;
    }

    public Date getDATE_AND_TIME() {
        return DATE_AND_TIME;
    }

    public void setDATE_AND_TIME(Date DATE_AND_TIME) {
        this.DATE_AND_TIME = DATE_AND_TIME;
    }

    public String getAUDITOR_NAME() {
        return AUDITOR_NAME;
    }

    public void setAUDITOR_NAME(String AUDITOR_NAME) {
        this.AUDITOR_NAME = AUDITOR_NAME;
    }

    public String getAUDITOR_CODE() {
        return AUDITOR_CODE;
    }

    public void setAUDITOR_CODE(String AUDITOR_CODE) {
        this.AUDITOR_CODE = AUDITOR_CODE;
    }

    public String getCLUSTER_NAME() {
        return CLUSTER_NAME;
    }

    public void setCLUSTER_NAME(String CLUSTER_NAME) {
        this.CLUSTER_NAME = CLUSTER_NAME;
    }

    public String getTSR_CODE() {
        return TSR_CODE;
    }

    public void setTSR_CODE(String TSR_CODE) {
        this.TSR_CODE = TSR_CODE;
    }

    public String getTSR_NAME() {
        return TSR_NAME;
    }

    public void setTSR_NAME(String TSR_NAME) {
        this.TSR_NAME = TSR_NAME;
    }

    public int getTOTAL_VISITED() {
        return TOTAL_VISITED;
    }

    public void setTOTAL_VISITED(int TOTAL_VISITED) {
        this.TOTAL_VISITED = TOTAL_VISITED;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getPICTURE_NAME() {
        return PICTURE_NAME;
    }

    public void setPICTURE_NAME(String PICTURE_NAME) {
        this.PICTURE_NAME = PICTURE_NAME;
    }

    public String getPICTURE_URL() {
        return PICTURE_URL;
    }

    public void setPICTURE_URL(String PICTURE_URL) {
        this.PICTURE_URL = PICTURE_URL;
    }

    public float getLatitude() {
        return Latitude;
    }

    public void setLatitude(float latitude) {
        Latitude = latitude;
    }

    public float getLongitude() {
        return Longitude;
    }

    public void setLongitude(float longitude) {
        Longitude = longitude;
    }

    public float getAccuracy() {
        return Accuracy;
    }

    public void setAccuracy(float accuracy) {
        Accuracy = accuracy;
    }

    public String getORIGINAL_DATE_STRING() {
        return ORIGINAL_DATE_STRING;
    }

    public void setORIGINAL_DATE_STRING(String ORIGINAL_DATE_STRING) {
        this.ORIGINAL_DATE_STRING = ORIGINAL_DATE_STRING;
    }
}
