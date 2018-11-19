package com.github.bkuzmic.web.db.pool;

public interface ConnectionPool {

    PgClosableConnection getConnection();
    boolean releaseConnection(PgClosableConnection connection);
    int getSize();
    void destroy();

}
