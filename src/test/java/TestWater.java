import diag.Watch;
import io.crm.web.template.EventPublisherForm;
import io.crm.web.template.Page;
import io.crm.web.template.PageBuilder;

import java.util.Collections;

/**
 * Created by someone on 08/09/2015.
 */
public class TestWater {
    public static void main(String... args) {
        String sql = "INSERT INTO br_checker_entrys " +
                "(" +
                "TRACKER_TABLE_ID, " +
                "CALL_NO, " +
                "CONSUMER_NAME, " +
                "CONSUMER_MOBILE_NUMBER, " +
                "NAME_MATCHED, " +
                "CALL_STATUS, " +
                "CONTACTED, " +
                "BAND, " +
                "TIME_OF_CHECK, " +
                "DATE_AND_TIME, " +
                "AUDITOR_NAME, " +
                "AUDITOR_CODE, " +
                "CLUSTER_NAME, " +
                "TSR_CODE, " +
                "TSR_NAME, " +
                "TOTAL_VISITED, " +
                "REMARK, " +
                "PICTURE_NAME, " +
                "PICTURE_URL, " +
                "Latitude, " +
                "Longitude, " +
                "Accuracy" +
                ") " +
                "VALUES " +
                "(" +
                "@TRACKER_TABLE_ID, " +
                "@CALL_NO, " +
                "@CONSUMER_NAME, " +
                "@CONSUMER_MOBILE_NUMBER, " +
                "@NAME_MATCHED, " +
                "@CALL_STATUS, " +
                "@CONTACTED, " +
                "@BAND, " +
                "@TIME_OF_CHECK, " +
                "@DATE_AND_TIME, " +
                "@AUDITOR_NAME, " +
                "@AUDITOR_CODE, " +
                "@CLUSTER_NAME, " +
                "@TSR_CODE, " +
                "@TSR_NAME, " +
                "@TOTAL_VISITED, " +
                "@REMARK, " +
                "@PICTURE_NAME, " +
                "@PICTURE_URL, " +
                "@Latitude, " +
                "@Longitude, " +
                "@Accuracy" +
                ")";

        System.out.println(sql);
    }
}
