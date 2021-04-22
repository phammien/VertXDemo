package com.example.starter;

import com.example.starter.model.Employee;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.HashMap;

public class VerticleA extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        /* Create a router Object*/
        Router router = Router.router(vertx);

        router.route("/").handler(this::hello);
        router.get("/abc").handler(this::testEventBus);

        /* Create the HTTP server */
        vertx
                .createHttpServer()
                .requestHandler(router)
                .listen(
                        // Retrieve the port from the configuration
                        config().getInteger("http.port", 8081),
                        result -> {
                            if (result.succeeded()) {
                                startPromise.complete();
                            } else {
                                startPromise.fail("Fail : " + result.cause());
                            }
                        }
                );
    }

    private void hello(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "text/html")
                .end("<h1>Hello Vertx</h1>");
    }

    private void testEventBus(RoutingContext routingContext) {
        vertx.eventBus().request("VerticleA", "", ar -> {

            System.out.println("Calling to Verticle B");

            if (ar.succeeded()) {
                System.out.println("Received reply: " + ar.result().body());
                routingContext.response().end(Json.encodePrettily(ar.result().body()));

            } else {
                System.out.println("Fail to call C : " + ar.cause().getMessage());
                routingContext.response().end(Json.encodePrettily("Failed"));
            }
        });

    }

}
