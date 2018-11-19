package com.github.bkuzmic.web.db;

import com.github.bkuzmic.web.db.exception.DbRuntimeException;
import com.github.bkuzmic.web.db.pool.ConnectionPool;
import com.github.bkuzmic.web.db.pool.PgClosableConnection;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

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

            DSLContext create = DSL.using(closableConnection.getConnection(), SQLDialect.POSTGRES);
            List<Long> ids = create.selectFrom("example.user").fetch("id", Long.class);

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
