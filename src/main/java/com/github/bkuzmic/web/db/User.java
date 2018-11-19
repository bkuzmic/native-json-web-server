package com.github.bkuzmic.web.db;

import java.util.Date;

public interface User {

    Long id();
    String username();
    String firstName();
    String lastName();
    Date createdTime();

}
