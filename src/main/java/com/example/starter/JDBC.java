package com.example.starter;

import io.vertx.core.Vertx;
import io.vertx.jdbcclient.JDBCConnectOptions;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.Tuple;

public class JDBC {

    public void configJDBC() {

        Vertx vertx = Vertx.vertx();

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
                    // failed to get a connection
                })
                .onSuccess(conn -> {
                    conn
                            .preparedQuery("SELECT * FROM user WHERE emp_id > ?")
                            // the emp_id to look up
                            .execute(Tuple.of(1000))
                            .onFailure(e -> {
                                // handle the failure

                                // very important! don't forget to return the connection
                                conn.close();
                            })
                            .onSuccess(rows -> {
                                for (Row row : rows) {
                                    System.out.println(row.getString("FIRST_NAME"));
                                }

                                // very important! don't forget to return the connection
                                conn.close();
                            });
                });


    }
}
