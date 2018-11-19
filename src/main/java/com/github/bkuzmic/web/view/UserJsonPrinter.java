package com.github.bkuzmic.web.view;

import com.github.bkuzmic.web.db.User;

import java.text.SimpleDateFormat;

public class UserJsonPrinter extends JsonPrinter<User> {

    private final SimpleDateFormat simpleDateFormat;

    public UserJsonPrinter() {
        super();
        simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
    }

    @Override
    public String toJson(User object) {
        return String.format(
                "{\"id\":%d, \"username\":\"%s\", \"first_name\":\"%s\", \"last_name\":\"%s\", \"created_time\":\"%s\"}",
                object.id(), object.username(), object.firstName(), object.lastName(),
                simpleDateFormat.format(object.createdTime()));
    }
}
