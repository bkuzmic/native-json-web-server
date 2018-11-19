package com.github.bkuzmic.web.db;

import java.util.Date;

public class ConstPgUser implements User {

    private final User origin;
    private final String username;
    private final String firstName;
    private final String lastName;
    private final Date createdTime;

    public ConstPgUser(User user, String username, String firstName, String lastName, Date createdTime) {
        this.origin = user;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdTime = createdTime;
    }

    @Override
    public Long id() {
        return this.origin.id();
    }

    @Override
    public String username() {
        return this.username;
    }

    @Override
    public String firstName() {
        return this.firstName;
    }

    @Override
    public String lastName() {
        return this.lastName;
    }

    @Override
    public Date createdTime() {
        return this.createdTime;
    }
}
