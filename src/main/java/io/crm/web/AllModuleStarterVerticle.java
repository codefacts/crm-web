package io.crm.web;

import io.crm.util.TaskCoordinator;
import io.crm.util.TaskCoordinatorBuilder;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

/**
 * Created by someone on 07/09/2015.
 */
final public class AllModuleStarterVerticle extends AbstractVerticle {
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        final TaskCoordinator taskCoordinator = new TaskCoordinatorBuilder().count(4)
                .onSuccess(() -> startFuture.complete())
                .onError(e -> startFuture.fail(e)).get();

        getVertx().deployVerticle(MainVerticle.class.getName(), taskCoordinator.add(v -> {
        }));
        getVertx().deployVerticle(io.crm.core.MainVerticle.class.getName(), taskCoordinator.add(v -> {
        }));
        getVertx().deployVerticle(io.crm.query.MainVerticle.class.getName(), taskCoordinator.add(v -> {
        }));
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        final TaskCoordinator taskCoordinator = new TaskCoordinatorBuilder().count(4)
                .onSuccess(() -> stopFuture.complete())
                .onError(e -> stopFuture.fail(e)).get();

        getVertx().undeploy(MainVerticle.class.getName(), taskCoordinator.add(v -> {
        }));
        getVertx().undeploy(io.crm.core.MainVerticle.class.getName(), taskCoordinator.add(v -> {
        }));
        getVertx().undeploy(io.crm.query.MainVerticle.class.getName(), taskCoordinator.add(v -> {
        }));
    }
}
