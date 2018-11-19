package com.github.bkuzmic.web.view;

import com.github.bkuzmic.web.db.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

public class UserJsonPrinterTest {

    @Test
    public void testToJson() {
        UserJsonPrinter printer = new UserJsonPrinter();

        User user = new User() {
            @Override
            public Long id() {
                return 1L;
            }

            @Override
            public String username() {
                return "test";
            }

            @Override
            public String firstName() {
                return "Bob";
            }

            @Override
            public String lastName() {
                return "Doe";
            }

            @Override
            public Date createdTime() {
                Calendar calendar = Calendar.getInstance();
                calendar.set(2018, 10, 15);
                return calendar.getTime();
            }
        };

        Assert.assertEquals("{\"id\":1, \"username\":\"test\", \"first_name\":\"Bob\", " +
                "\"last_name\":\"Doe\", \"created_time\":\"15.11.2018\"}", printer.toJson(user));
    }

}
