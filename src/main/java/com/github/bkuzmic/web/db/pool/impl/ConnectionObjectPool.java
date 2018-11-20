package com.github.bkuzmic.web.db.pool.impl;

import com.github.bkuzmic.web.db.pool.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionObjectPool extends ObjectPool<Connection> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionObjectPool.class);

    private final ConnectionFactory factory;

    public ConnectionObjectPool(long expirationTime, ConnectionFactory connectionFactory) {
        super(expirationTime);
        this.factory = connectionFactory;
    }

    @Override
    protected Connection create() {
        return this.factory.getConnection();
    }

    @Override
    public boolean validate(Connection o) {
        try {
            return (!o.isClosed() && o.isValid(0));
        } catch (SQLException e) {
            LOGGER.error("Error validating connection from pool: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void expire(Connection o) {
        try {
            o.close();
        } catch (SQLException e) {
            LOGGER.warn("Error expiring connection from pool: {}", e.getMessage(), e);
        }
    }
}
