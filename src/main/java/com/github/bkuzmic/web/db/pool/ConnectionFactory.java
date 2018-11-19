package com.github.bkuzmic.web.db.pool;

import java.sql.Connection;

public interface ConnectionFactory {

    Connection getConnection();

}
