package com.github.bkuzmic.web;


import com.github.bkuzmic.web.db.*;
import com.github.bkuzmic.web.db.pool.BasicPgConnectionPool;
import com.github.bkuzmic.web.db.pool.ConnectionFactory;
import com.github.bkuzmic.web.db.pool.ConnectionPool;
import com.github.bkuzmic.web.db.pool.PgConnectionFactory;
import com.github.bkuzmic.web.view.JsonHttpServerExchange;
import com.github.bkuzmic.web.view.JsonResponse;
import com.github.bkuzmic.web.view.StatsJsonPrinter;
import com.github.bkuzmic.web.view.UserJsonPrinter;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private static ConnectionPool pgPool;
    private static Undertow server;

    public static void main(String... args) {
        LOGGER.info("Starting server at {}", new Date());
        registerShutDownHook();
        new Main().startEndpoints();
    }

    private static void registerShutDownHook() {
        final Thread mainThread = Thread.currentThread();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (server != null) {
                LOGGER.info("Stopping web server");
                server.stop();
            }
            if (pgPool != null) {
                LOGGER.info("Destroying PG connection pool");
                pgPool.destroy();
            }
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    private void startEndpoints() {
        LOGGER.info("Starting web server");
        long startTime = System.nanoTime();

        ConnectionFactory pgConnectionFactory =
                new PgConnectionFactory(getPostgresUrl(), getDatabaseUsername(), getDatabasePassword());

        pgPool = new BasicPgConnectionPool(pgConnectionFactory, getDatabaseMaxConnections());

        HttpHandler usersHandler = this::fetchAllUsers;

        HttpHandler userIdHandler = exchange -> {
            if (!exchange.getQueryParameters().isEmpty()) {
                String userIdString = exchange.getQueryParameters().get("userId").getFirst();
                if (userIdString != null && !userIdString.equals("")) {
                    // return user with provided id
                    Integer userId = Integer.parseInt(userIdString);
                    LOGGER.info("Fetching user with id: {}", userId);
                    User user =
                            new PgUser(pgPool, Long.valueOf(userId));
                    ConstPgUser constPgUser =
                            new ConstPgUser(user, user.username(), user.firstName(), user.lastName(), user.createdTime());

                    new JsonResponse<>(new UserJsonPrinter(), new JsonHttpServerExchange(exchange)).toJson(constPgUser);
                } else {
                    // return all users
                    fetchAllUsers(exchange);
                }
            }

        };

        HttpHandler statsHandler = exchange -> {
            LOGGER.info("Get pool statistics");
            new JsonResponse<>(new StatsJsonPrinter(),  new JsonHttpServerExchange(exchange)).toJson(pgPool.getSize());
        };

        RoutingHandler routes = new RoutingHandler()
                .get("/user", usersHandler)
                .get("/user/{userId}", userIdHandler)
                .get("/stats", statsHandler);

        server = Undertow.builder()
                .addHttpListener(4567, "0.0.0.0")
                .setHandler(routes)
                            .build();
        server.start();

        long endTime = System.nanoTime();
        LOGGER.info("Started web server in {}ms", String.format("%.2f", (endTime-startTime)/1000000f));
    }

    private void fetchAllUsers(HttpServerExchange exchange) {
        LOGGER.info("Fetching all users from database");
        List<User> userList = new ArrayList<>();

        Users users = new ConstPgUsers(pgPool);
        for (User user : users.iterate()) {
            userList.add(user);
        }

        new JsonResponse<>(new UserJsonPrinter(),  new JsonHttpServerExchange(exchange)).toJsonList(userList);
    }

    private String getPostgresUrl() {
        return System.getenv("PG_DATABASE_URL");
    }
    private String getDatabaseUsername() {
        return System.getenv("PG_DATABASE_USERNAME");
    }
    private String getDatabasePassword() {
        return System.getenv("PG_DATABASE_PASSWORD");
    }
    private int getDatabaseMaxConnections() {
        return Integer.parseInt(System.getenv("PG_DATABASE_MAX_CONNECTIONS"));
    }

}