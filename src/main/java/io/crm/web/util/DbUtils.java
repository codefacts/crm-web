package io.crm.web.util;

import io.crm.intfs.BiConsumerUnchecked;
import io.crm.intfs.FunctionUnchecked;
import io.crm.promise.Promises;
import io.crm.promise.intfs.Defer;
import io.crm.promise.intfs.Promise;
import io.crm.util.Util;
import io.crm.util.touple.immutable.Tpl2;
import io.crm.util.touple.immutable.Tpls;
import io.vertx.core.Vertx;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

/**
 * Created by someone on 06/12/2015.
 */
final public class DbUtils {
    public static Promise<SQLConnection> getConnection(final JDBCClient jdbcClient) {
        final Defer<SQLConnection> defer = Promises.defer();
        jdbcClient.getConnection(Util.makeDeferred(defer));
        return defer.promise();
    }

    public static <T> Promise<T> query(final Vertx vertx, final EntityManagerFactory emf, FunctionUnchecked<EntityManager, T> callableUnchecked) {
        return Util.executeBlocking(vertx, () -> {
            EntityManager em = null;
            try {
                em = emf.createEntityManager();
                return callableUnchecked.apply(em);
            } finally {
                em.close();
            }
        });
    }

    public static Promise<Void> update(final Vertx vertx, final EntityManagerFactory emf, BiConsumerUnchecked<EntityManager, EntityTransaction> consumerUnchecked) {
        final Defer<Void> defer = Promises.defer();
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            consumerUnchecked.accept(em, tx);
            tx.commit();
            defer.complete();
        } catch (final Exception ex) {
            if (tx != null && tx.isActive()) tx.rollback();
            defer.fail(ex);
        } finally {
            em.close();
        }
        return defer.promise();
    }

    public static void main() {
        final Tpl2<Integer, Integer> of = Tpls.of(1, 3);
    }
}
