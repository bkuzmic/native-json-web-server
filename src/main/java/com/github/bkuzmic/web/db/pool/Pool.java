package com.github.bkuzmic.web.db.pool;

public interface Pool<T> {

    T checkOut();
    void checkIn(T object);
    int size();
    void destroy();

}
