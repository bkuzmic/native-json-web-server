package com.github.bkuzmic.web.db.pool;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;

public class PgClosableConnection implements AutoCloseable {

    private final Connection connection;
    private final ConnectionPool connectionPool;

    public PgClosableConnection(Connection connection, ConnectionPool connectionPool) {
        this.connection = connection;
        this.connectionPool = connectionPool;
    }

    public Connection connection() {
        return this.connection;
    }

    public DSLContext context() {
        return DSL.using(this.connection, SQLDialect.POSTGRES);
    }

    @Override
    public void close() throws Exception {
        this.connectionPool.releaseConnection(this);
    }

}
