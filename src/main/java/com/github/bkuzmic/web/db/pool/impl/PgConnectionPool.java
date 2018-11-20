package com.github.bkuzmic.web.db.pool.impl;

import com.github.bkuzmic.web.db.pool.ConnectionPool;
import com.github.bkuzmic.web.db.pool.Pool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class PgConnectionPool implements ConnectionPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(PgConnectionPool.class);

    private final Pool<Connection> pool;

    public PgConnectionPool(Pool<Connection> pool) {
        this.pool = pool;
    }

    @Override
    public PgClosableConnection getConnection() {
        LOGGER.debug("Get connection from pool");
        return new PgClosableConnection(this.pool.checkOut(), this);
    }

    @Override
    public void releaseConnection(PgClosableConnection connection) {
        LOGGER.debug("Release connection to pool");
        this.pool.checkIn(connection.connection());
    }

    @Override
    public int size() {
        return this.pool.size();
    }

    @Override
    public void destroy() {
        this.pool.destroy();
    }
}
