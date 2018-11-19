package com.github.bkuzmic.web.db.pool;

import com.github.bkuzmic.web.db.exception.DbRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BasicPgConnectionPool implements ConnectionPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasicPgConnectionPool.class);
    private static final int INITIAL_POOL_SIZE = 1;
    private static final int DEFAULT_MAX_POOL_SIZE = 10;

    private final ConnectionFactory connectionFactory;
    private final int maxPoolSize;

    private List<PgClosableConnection> connectionPool;
    private List<PgClosableConnection> usedConnections = new ArrayList<>();

    public BasicPgConnectionPool(ConnectionFactory connectionFactory, int maxPoolSize) {
        this.connectionFactory = connectionFactory;
        this.maxPoolSize = (maxPoolSize <= INITIAL_POOL_SIZE) ? DEFAULT_MAX_POOL_SIZE : maxPoolSize;
    }

    private PgClosableConnection createConnection() {
        return new PgClosableConnection(connectionFactory.getConnection(), this);
    }

    @Override
    public PgClosableConnection getConnection() {
        LOGGER.debug("Get connection from pool");
        synchronized (this) {
            if (connectionPool == null) {
                // initialize pool lazy
                LOGGER.debug("Initialize connection pool to size: {}", INITIAL_POOL_SIZE);
                connectionPool = new ArrayList<>(INITIAL_POOL_SIZE);
                for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                    connectionPool.add(createConnection());
                }
            }
        }
        if (connectionPool.isEmpty()) {
            LOGGER.debug("Pool is empty, create new connection");
            if (usedConnections.size() < maxPoolSize) {
                connectionPool.add(createConnection());
            } else {
                LOGGER.error("\"Maximum pool size of {} reached, no available connections!\"", maxPoolSize);
                throw new DbRuntimeException(
                        "Maximum pool size reached, no available connections!");
            }
        }

        PgClosableConnection connection = null;
        while (!connectionPool.isEmpty()) {
            try {
                connection = connectionPool
                    .remove(connectionPool.size() - 1);
                if (connection.getConnection().isValid(0)) {
                    break;
                }
            } catch (SQLException e) {
                LOGGER.error("Error getting connection from pool: {}", e.getMessage(), e);
                throw new DbRuntimeException(e);
            }
        }
        if (connection == null) {
            connection = createConnection();
        }
        usedConnections.add(connection);
        return connection;
    }

    @Override
    public boolean releaseConnection(PgClosableConnection connection) {
        connectionPool.add(connection);
        return usedConnections.remove(connection);
    }

    @Override
    public int getSize() {
        if (connectionPool != null) {
            LOGGER.info("Connection pool size: {}", connectionPool.size() + usedConnections.size());
            return connectionPool.size() + usedConnections.size();
        }
        return 0;
    }

    @Override
    public void destroy() {
        try {
            if (connectionPool != null) {
                for (PgClosableConnection connection : connectionPool) {
                    connection.getConnection().close();
                }
                connectionPool.clear();
            }
            for (PgClosableConnection connection : usedConnections) {
                connection.getConnection().close();
            }
            usedConnections.clear();
        } catch (Exception e){
            LOGGER.error("Exception on destroy: {}", e.getMessage(), e);
        }
    }
}
