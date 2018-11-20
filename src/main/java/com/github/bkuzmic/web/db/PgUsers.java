package com.github.bkuzmic.web.db;

import com.github.bkuzmic.web.db.exception.DbRuntimeException;
import com.github.bkuzmic.web.db.pool.ConnectionPool;
import com.github.bkuzmic.web.db.pool.impl.PgClosableConnection;

import java.util.ArrayList;
import java.util.List;

public class PgUsers implements Users {

    private final ConnectionPool pool;

    public PgUsers(ConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Iterable<User> iterate() {
        try (PgClosableConnection closableConnection = this.pool.getConnection()){
            List<Long> ids =  closableConnection.context().selectFrom("example.user").fetch("id", Long.class);

            List<User> users = new ArrayList<>();
            for (Long id : ids) {
                User user = new PgUser(this.pool, id);
                users.add(user);
            }

            return users;
        } catch (Exception e) {
            throw new DbRuntimeException(e);
        }
    }

}
