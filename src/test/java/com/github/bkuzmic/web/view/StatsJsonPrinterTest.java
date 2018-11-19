package com.github.bkuzmic.web.view;

import org.junit.Assert;
import org.junit.Test;

public class StatsJsonPrinterTest {

    @Test
    public void testToJson() {
        StatsJsonPrinter printer = new StatsJsonPrinter();
        Assert.assertEquals("{\"connection_pool_size\":1}", printer.toJson(1));
    }

}
