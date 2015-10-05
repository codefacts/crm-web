import diag.Watch;
import io.crm.web.template.EventPublisherForm;
import io.crm.web.template.Page;
import io.crm.web.template.PageBuilder;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collections;

/**
 * Created by someone on 08/09/2015.
 */
public class TestWater {
    public static void main(String... args) throws Exception {
        PrintStream out = new PrintStream(new FileOutputStream("C:\\Users\\skpaul\\Desktop\\BrChecker\\saidul_1000_04-Oct-2015 125101 PM.csv"));
        for (int i = 0; i < 500; i++) {
            out.println("\"1\",\"1\",\"1\",\"call\",\"01675349888\",\"Wrong Number\",\"Yes\",\"Yes\",\"Yes\",\"04-Oct-2015 12:50 PM\",\"04-Oct-2015 12:50:54 PM\",\"1\",\"saidul\",\"1000\",\"clu name\",\"1710\",\"vbsbs\",\"10\",\"noixbbd\",\"TSR_Name vbsbs-TSR Code 1710-Time 04-10-2015_124955_PM-Cluster_Name clu name-Auditor_Name saidul-Auditor_Code 1000.PNG\",\"23.7828994824633\",\"90.3979762833098\",\"16.0\"");
        }
        System.out.println("DONE");
    }
}
