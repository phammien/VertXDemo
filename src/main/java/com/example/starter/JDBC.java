package com.example.starter;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.jdbcclient.JDBCConnectOptions;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;

public class JDBC {

    public static void configJDBC(Vertx vertx) {

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

//      String sql = "CREATE TABLE TESTCONNECTDB (ID int generated by default as identity (start with 1 increment by 1) not null, NAME varchar(255), EMAIL varchar(255), AGE int)";
        String insert = "INSERT INTO TESTCONNECTDB (NAME, EMAIL, AGE) VALUES ('employee3', 'e3@gmail.com', 20)";
        String delete = "DELETE FROM TESTCONNECTDB WHERE NAME = 'employee1'";
        String queryAll = "SELECT * FROM TESTCONNECTDB";
        exampleQueryManual(pool, queryAll)
                .compose(nextItem -> exampleQueryManual(pool, insert)
                        .compose(nextItem1 -> exampleQueryManual(pool, queryAll)
                                .compose(nextItem2 -> exampleQueryManual(pool, delete)
                                        .compose(nextItem3 -> exampleQueryManual(pool, queryAll)))));
    }

    public static Future<RowSet<Row>> exampleQueryManual(JDBCPool pool, String sql) {

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

                                if(rows != null) {
                                    for (Row row : rows) {
                                        System.out.println("Result: " + row.getString("NAME"));
                                    }
                                }

                                // very important! don't forget to return the connection
                                conn.close();
                                promise.complete(rows);
                            });
                });

        return promise.future();
    }

    public static void main(String[] args) {
        configJDBC(Vertx.vertx());
    }
}
