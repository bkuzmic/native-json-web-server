package com.github.bkuzmic.web.db.pool;

import com.github.bkuzmic.web.db.pool.impl.PgClosableConnection;

public interface ConnectionPool {

    PgClosableConnection getConnection();
    void releaseConnection(PgClosableConnection connection);
    int size();
    void destroy();

}
