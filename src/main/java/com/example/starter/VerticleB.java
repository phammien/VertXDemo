package com.example.starter;

import com.example.starter.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
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

        vertx.eventBus().consumer("VerticleB").handler(message -> {

            System.out.println("Listening VerticleA ... ");
//            message.reply("abc");

            vertx.eventBus().request("VerticleC", "", ar -> {
                if(ar.succeeded()) {
                    System.out.println("call  success");
                    message.reply(ar.result().body());
                }
            });
        });
        startPromise.complete();
    }
}
