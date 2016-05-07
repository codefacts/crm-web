package webcomposer.chain.cleanups;

import io.crm.promise.Promises;
import io.crm.promise.intfs.Defer;
import io.crm.promise.intfs.Promise;
import io.crm.util.Context;
import io.crm.util.Util;
import io.vertx.core.eventbus.Message;
import io.vertx.ext.sql.SQLConnection;
import webcomposer.Cnst;

import java.util.function.BiFunction;

/**
 * Created by shahadat on 5/3/16.
 */
public class DbCleaner<T> implements BiFunction<Context, T, Promise<Void>> {
    @Override
    public Promise<Void> apply(Context context, T message) {
        final Defer<Void> defer = Promises.defer();
        if (context.containsKey(Cnst.CON)) {
            final SQLConnection connection = context.getAs(Cnst.CON);
            connection.close(Util.makeDeferred(defer));
        }
        return defer.promise();
    }
}
