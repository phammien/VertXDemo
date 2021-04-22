package com.example.starter;

import com.example.starter.model.Employee;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.Json;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VerticleC extends AbstractVerticle {

  private HashMap<Integer, Employee> employeesMap = new HashMap<>();

  private void createMap() {
    employeesMap.put(1, new Employee("e1", "employee1", "e1@gmail.com", 23));
    employeesMap.put(2, new Employee("e2", "employee2", "e2@gmail.com", 24));
    employeesMap.put(3, new Employee("e3", "employee3", "e3@gmail.com", 25));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    /* Create data test*/
    createMap();

    EventBus eb = vertx.eventBus();

    /* Get massage from B and reply list employee for B */
    MessageConsumer<String> consumer = eb.consumer("VerticleB");
    consumer.handler(message -> {

      System.out.println("Reply to B : " + Json.encodePrettily(employeesMap.values()));
      message.reply(Json.encodePrettily(employeesMap.values()));
    });
  }
}
