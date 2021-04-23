package com.example.starter;

import com.example.starter.config.SQLConst;
import com.example.starter.model.Employee;
import io.netty.util.internal.StringUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.jdbcclient.JDBCConnectOptions;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.desc.ColumnDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class VerticleC extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        EventBus eb = vertx.eventBus();

        /* Get massage from B and reply list employee for B */
        MessageConsumer<String> consumer = eb.consumer("VerticleC");
        consumer.handler(message -> {

            /*  */
            JDBCPool pool = JDBCPool.pool(
                    vertx,
                    // configure the connection
                    new JDBCConnectOptions()
                            // H2 connection string
                            .setJdbcUrl("jdbc:oracle:thin:@//172.16.13.10:1521/umarketuat")
                            // username
                            .setUser("soap_admin")
                            // password
                            .setPassword("1234567"),
                    // configure the pool
                    new PoolOptions().setMaxSize(16)
            );

            pool
                    .getConnection()
                    .onFailure(e -> {
                        System.out.println("Query Manual Fail : " + e);
                    })
                    .onSuccess(conn -> {
                        System.out.println("Prepared Query Manual Success");

                        // insert
                        conn
                                .query(SQLConst.queryAll)
                                .execute()
                                .onFailure(e -> {
                                    System.out.println("Query fail : " + e);
                                    conn.close();
                                })
                                .onSuccess(rows -> {
                                    System.out.println("Query success");

                                    List<Employee> result = new ArrayList<>();

                                    if (rows != null) {
                                        for (Row row : rows) {
                                            Employee employee = new Employee();
                                            employee.setId(row.getInteger("ID").toString());
                                            employee.setName(row.getString("NAME"));
                                            employee.setEmail(row.getString("EMAIL"));
                                            employee.setAge(row.getInteger("AGE"));
                                            result.add(employee);
                                        }
                                    }

                                    System.out.println("Reply to B : " + Json.encodePrettily(result));
                                    message.reply(Json.encodePrettily(result));

                                    // very important! don't forget to return the connection
                                    conn.close();
                                });
                    });
        });
        startPromise.complete();
    }
}
