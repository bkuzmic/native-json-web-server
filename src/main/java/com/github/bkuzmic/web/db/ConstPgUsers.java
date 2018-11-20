package com.github.bkuzmic.web.db;

import com.github.bkuzmic.web.db.exception.DbRuntimeException;
import com.github.bkuzmic.web.db.pool.ConnectionPool;
import com.github.bkuzmic.web.db.pool.PgClosableConnection;
import org.jooq.Record;
import org.jooq.Result;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConstPgUsers implements Users {

    private final ConnectionPool pool;

    public ConstPgUsers(ConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Iterable<User> iterate() {
        try (PgClosableConnection closableConnection = this.pool.getConnection()){
            Result<Record> result = closableConnection.context().selectFrom("example.user").fetch();

            List<User> users = new ArrayList<>();
            for (Record record : result) {
                User user = new ConstPgUser(
                        new PgUser(this.pool, record.getValue("id", Long.class)),
                        record.getValue("username", String.class),
                        record.getValue("first_name", String.class),
                        record.getValue("last_name", String.class),
                        new Date(record.getValue("created_time", OffsetDateTime.class).toInstant().toEpochMilli()));
                users.add(user);
            }
            return users;
        } catch (Exception e) {
            throw new DbRuntimeException(e);
        }
    }
}
