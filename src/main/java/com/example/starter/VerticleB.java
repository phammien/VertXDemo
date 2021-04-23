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

//        vertx.eventBus().request("VerticleB", "", ar -> {
//
//            System.out.println("Calling to Verticle C");
//
//            if (ar.succeeded()) {
//
//                System.out.println("Received reply from C: " + ar.result().body());
//
//                vertx.eventBus().consumer("VerticleA").handler(message -> {
//                    message.reply(ar.result().body());
//                });
//
//            } else {
//                System.out.println("Fail to call C : " + ar.cause().getMessage());
//            }
//        });
    }

//    public Future<Void> registerConsumer() {
//
//        Promise promise = Promise.promise();
//
//        eventBus.consumer("VerticleA").handler(message -> {
//            message.reply("VerticleB receive a message from Verticle A : " + message.body());
//        });
//
//        return promise.future();
//    }
//
//    public Future<String> receiveMessage() {
//
//        Promise<String> promise = Promise.promise();
//
//        eventBus.request("VerticleC", "", ar -> {
//            if(ar.succeeded()) {
//                System.out.println("Receive a message from VerticleC : " + Json.encodePrettily(ar.result().body()));
//                promise.complete(Json.encodePrettily(ar.result().body()));
//            } else {
//                System.out.println("Call VerticleC fail : " + ar.cause().getMessage());
//            }
//        });
//
//        return promise.future();
//    }
}
