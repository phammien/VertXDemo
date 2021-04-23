package com.example.starter;

import com.example.starter.model.Employee;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.jdbcclient.JDBCConnectOptions;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

import java.util.ArrayList;
import java.util.List;

public class JDBC {

    private JDBCPool pool;

    public Future<Void> configJDBC(Vertx vertx) {

        Promise<Void> promise = Promise.promise();

        pool = JDBCPool.pool(
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
        promise.complete();
        return promise.future();
    }

    public Future<RowSet<Row>> query(String sql) {

        Promise<RowSet<Row>> promise = Promise.promise();

        pool
                .getConnection()
                .onFailure(e -> {
                    System.out.println("Query Manual Fail : " + e);
                })
                .onSuccess(conn -> {
                    System.out.println("Prepared Query Manual Success");

                    // insert
                    conn
                            .query(sql)
                            .execute()
                            .onFailure(e -> {
                                System.out.println("Query fail : " + e);
                                conn.close();
                            })
                            .onSuccess(rows -> {
                                System.out.println("Query success");

                                // very important! don't forget to return the connection
                                conn.close();
                                promise.complete(rows);
                            });
                });

        return promise.future();
    }
}
