package com.example.starter;

import com.example.starter.config.SQLConst;
import com.example.starter.config.VerticleConfig;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class Main {

    private static Vertx vertx = Vertx.vertx();

    public static void main(String[] args) {

//        vertx.deployVerticle(VerticleConfig.verticleA);
//        vertx.deployVerticle(VerticleConfig.verticleB);
//        vertx.deployVerticle(VerticleConfig.verticleC);

        vertx.deployVerticle(VerticleConfig.verticleC)
                .compose(itemNext -> vertx.deployVerticle(VerticleConfig.verticleB))
                .compose(itemNext -> vertx.deployVerticle(VerticleConfig.verticleA))
        .onSuccess(resp -> {
            System.out.println("Deploy OK");
        })
        .onFailure(resp -> {
            System.out.println("Deploy Fail : " + resp.getCause().getMessage());
        });

    }

}
