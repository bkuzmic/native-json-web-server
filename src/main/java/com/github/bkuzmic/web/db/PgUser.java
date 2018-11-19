package com.github.bkuzmic.web.db;

import com.github.bkuzmic.web.db.exception.DbRuntimeException;
import com.github.bkuzmic.web.db.pool.ConnectionPool;
import com.github.bkuzmic.web.db.pool.PgClosableConnection;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.Date;

public class PgUser implements User {

    private final Long id;
    private final ConnectionPool pool;

    public PgUser(ConnectionPool pool, Long id) {
        this.id = id;
        this.pool = pool;
    }

    @Override
    public Long id() {
        return this.id;
    }

    @Override
    public String username() {
        return getSingleResult("username", String.class);
    }

    @Override
    public String firstName() {
        return getSingleResult("first_name", String.class);
    }

    @Override
    public String lastName() {
        return getSingleResult("last_name", String.class);
    }

    @Override
    public Date createdTime() {
        OffsetDateTime createdTime = getSingleResult("created_time", OffsetDateTime.class);
        if (createdTime != null) {
            return new Date(createdTime.toInstant().toEpochMilli());
        }
        return new Date(0);
    }

    private <T> T getSingleResult(String field, Class<T> type) {
        try (PgClosableConnection closableConnection = this.pool.getConnection()){
            DSLContext create = DSL.using(closableConnection.getConnection(), SQLDialect.POSTGRES);
            return create.selectFrom("example.user").where("id = {0}", this.id).fetchOne(field, type);
        } catch (Exception e) {
            throw new DbRuntimeException(e);
        }
    }


}
