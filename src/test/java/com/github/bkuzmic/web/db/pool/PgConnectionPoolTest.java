package com.github.bkuzmic.web.db.pool;

import com.github.bkuzmic.web.db.pool.impl.PgConnectionPool;
import com.github.bkuzmic.web.db.pool.impl.PgClosableConnection;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class PgConnectionPoolTest {

    private ConnectionPool pool;
    private Connection mockConnection;
    private Pool<Connection> mockPool;

    @Before
    public void setUp() throws Exception {
        mockConnection = mock(Connection.class);

        mockPool = mock(Pool.class);
        when(mockPool.checkOut()).thenReturn(mockConnection);

        this.pool = new PgConnectionPool(mockPool);
    }

    @Test
    public void testGetConnection() {
        Assert.assertNotNull(this.pool.getConnection());
        verify(mockPool, times(1)).checkOut();
    }

    @Test
    public void testReleaseConnection() {
        PgClosableConnection closableConnection = new PgClosableConnection(mockConnection, this.pool);
        this.pool.releaseConnection(closableConnection);
        verify(mockPool).checkIn(eq(mockConnection));
    }

    @Test
    public void testDestroy() {
        this.pool.destroy();
        verify(mockPool).destroy();
    }

}
