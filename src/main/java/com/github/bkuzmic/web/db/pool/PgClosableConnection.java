package com.github.bkuzmic.web.db.pool;

import java.sql.Connection;

public class PgClosableConnection implements AutoCloseable {

    private final Connection connection;
    private final ConnectionPool connectionPool;

    public PgClosableConnection(Connection connection, ConnectionPool connectionPool) {
        this.connection = connection;
        this.connectionPool = connectionPool;
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws Exception {
        this.connectionPool.releaseConnection(this);
    }

}
