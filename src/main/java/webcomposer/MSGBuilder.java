package webcomposer;

import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.sql.SQLConnection;

public class MSGBuilder<T> {
    private T body;
    private DeliveryOptions deliveryOptions = null;
    private Message message;
    private Store store = null;
    private SQLConnection connection;

    public MSGBuilder<T> setBody(T body) {
        this.body = body;
        return this;
    }

    public MSGBuilder<T> setDeliveryOptions(DeliveryOptions deliveryOptions) {
        this.deliveryOptions = deliveryOptions;
        return this;
    }

    public MSGBuilder<T> setMessage(Message message) {
        this.message = message;
        return this;
    }

    public MSGBuilder<T> setStore(Store store) {
        this.store = store;
        return this;
    }

    public MSGBuilder<T> setConnection(SQLConnection connection) {
        this.connection = connection;
        return this;
    }

    public SQLConnection getConnection() {
        return connection;
    }

    public MSG<T> build() {
        return new MSG<>(body, deliveryOptions, message, store, connection);
    }
}