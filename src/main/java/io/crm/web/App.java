package io.crm.web;

import io.vertx.core.Vertx;

/**
 * Created by someone on 07/09/2015.
 */
public class App {
    public static void main(String... args) {
        Vertx.vertx().deployVerticle(new MainVerticle());
    }
}
