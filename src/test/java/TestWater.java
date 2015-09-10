import diag.Watch;
import io.crm.web.view.EventPublisherForm;
import io.crm.web.view.Page;
import io.crm.web.view.PageBuilder;

import java.util.Collections;

/**
 * Created by someone on 08/09/2015.
 */
public class TestWater {
    public static void main(String... args) {
        Watch watch = new Watch().start();
        for (int i = 0; i < 1_00_000; i++) {
            final EventPublisherForm eventPublisherForm = new EventPublisherForm(Collections.EMPTY_LIST, null, null, null, null);
            final Page ttLL = new PageBuilder("TtLL").body(eventPublisherForm).build();
            final String render = ttLL.render();
//            System.out.println(render);
        }
        System.out.println(watch.end().elapsed());
    }
}
