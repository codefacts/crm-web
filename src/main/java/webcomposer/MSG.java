package webcomposer;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.sql.SQLConnection;

import java.util.Objects;

/**
 * Created by shahadat on 5/4/16.
 */
final public class MSG<T> {
    public final T body;
    public final DeliveryOptions deliveryOptions;
    public final Message message;
    public final Store store;
    public final SQLConnection connection;

    MSG(T body, DeliveryOptions deliveryOptions, Message message, Store store, SQLConnection connection) {
        Objects.requireNonNull(message);
        this.body = body;
        this.deliveryOptions = deliveryOptions;
        this.message = message;
        this.store = store == null ? new Store() : store;
        this.connection = connection;
    }

    public MSG(T body, Message message, SQLConnection connection) {
        this(body, null, message, null, connection);
    }

    public static <T> MSG<T> create(T body, DeliveryOptions deliveryOptions, Message message, Store store) {
        return new MSGBuilder<T>().setBody(body).setDeliveryOptions(deliveryOptions).setMessage(message).setStore(store).build();
    }

    public <R> MSGBuilder<R> builder() {
        return new MSGBuilder<R>()
            .setBody(null)
            .setDeliveryOptions(deliveryOptions)
            .setMessage(message)
            .setStore(store)
            ;
    }
}
