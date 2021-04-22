package com.example.starter;

import com.example.starter.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class VerticleB extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        vertx.eventBus().request("VerticleB", "", ar -> {

            System.out.println("Calling to Verticle C");

            if (ar.succeeded()) {

                System.out.println("Received reply from C: " + ar.result().body());

                vertx.eventBus().consumer("VerticleA").handler(message -> {
                    message.reply(ar.result().body());
                });

            } else {
                System.out.println("Fail to call C : " + ar.cause().getMessage());
            }
        });
    }
}
