package com.github.bkuzmic.web.db.pool;

import com.github.bkuzmic.web.db.exception.DbRuntimeException;

import java.sql.Connection;
import java.sql.DriverManager;

public class PgConnectionFactory implements ConnectionFactory {

    private final String url;
    private final String user;
    private final String password;

    public PgConnectionFactory(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public Connection getConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(this.url, this.user, this.password);
        } catch (Exception e) {
            throw new DbRuntimeException("PostgreSQL connection not available", e);
        }
    }
}
