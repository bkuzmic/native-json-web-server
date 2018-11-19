package com.github.bkuzmic.web.db.pool;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BasicPgConnectionPoolTest {

    private ConnectionPool pool;

    @Before
    public void setUp() throws Exception {
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.isValid(eq(0))).thenReturn(true);

        ConnectionFactory mockConnectionFactory = mock(ConnectionFactory.class);
        when(mockConnectionFactory.getConnection()).thenReturn(mockConnection);

        this.pool = new BasicPgConnectionPool(mockConnectionFactory, 5);
    }

    @Test
    public void testGetConnection() {
        Assert.assertNotNull(this.pool.getConnection());
        Assert.assertEquals(1, this.pool.getSize());

        this.pool.getConnection();
        Assert.assertEquals(2, this.pool.getSize());
    }

    @Test
    public void testReleaseConnection() {
        PgClosableConnection closableConnection = this.pool.getConnection();
        Assert.assertEquals(true, this.pool.releaseConnection(closableConnection));
    }

    @Test
    public void testDestroy() {
        this.pool.destroy();
        Assert.assertEquals(0, this.pool.getSize());
    }

}
