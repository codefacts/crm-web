import io.crm.model.Consumer;

import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by someone on 08/09/2015.
 */
public class TestWater {
    public static void main(String... args) throws Exception {
        onReady(new java.util.function.Consumer<Integer>() {
            @Override
            public void accept(Integer size) {
                System.out.print(size);
            }
        });
    }

    public static void onReady(java.util.function.Consumer<Integer> consumer) {
        consumer.accept(10);
    }
}
