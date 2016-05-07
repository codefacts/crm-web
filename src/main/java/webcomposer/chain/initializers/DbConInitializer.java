package webcomposer.chain.initializers;

import com.google.common.collect.ImmutableMap;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Defer;
import io.crm.promise.intfs.Promise;
import io.crm.util.Util;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import java.util.Map;
import java.util.function.Function;

/**
 * Created by shahadat on 5/3/16.
 */
public class DbConInitializer<T> implements Function<T, Promise<Map<String, Object>>> {
    private final JDBCClient jdbcClient;

    public DbConInitializer(Vertx vertx, JDBCClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Promise<Map<String, Object>> apply(T message) {

        final Defer<SQLConnection> defer = Promises.defer();

        jdbcClient.getConnection(Util.makeDeferred(defer));

        return defer.promise().mapToPromise(con -> {
            try {
                return Promises.<Map<String, Object>>from(ImmutableMap.of("con", con));
            } catch (Exception ex) {
                con.close();
                return Promises.<Map<String, Object>>fromError(ex);
            }
        });
    }
}
