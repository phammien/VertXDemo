package com.example.starter;

import com.example.starter.config.VerticleConfig;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class Main {

    private static Vertx vertx = Vertx.vertx();

    public static void main(String[] args) {

        vertx.deployVerticle(VerticleConfig.verticleA,recs -> {
            if(recs.succeeded()) {
                vertx.deployVerticle(VerticleConfig.verticleC);
                vertx.deployVerticle(VerticleConfig.verticleB);

            } else {
                System.out.println("deploy A fail : " + recs.cause().getMessage());
            }
        });

    }
}
