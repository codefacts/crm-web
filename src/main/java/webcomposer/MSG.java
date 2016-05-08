package webcomposer;

import io.vertx.core.eventbus.DeliveryOptions;

/**
 * Created by shahadat on 5/4/16.
 */
final public class MSG<T> {
    public final T body;
    public final DeliveryOptions deliveryOptions;

    private MSG(T body, DeliveryOptions deliveryOptions) {
        this.body = body;
        this.deliveryOptions = deliveryOptions;
    }

    public MSG(T body) {
        this(body, null);
    }

    public static <T> MSG<T> create(T body, DeliveryOptions deliveryOptions) {
        return new MSG<T>(body, deliveryOptions);
    }
}
